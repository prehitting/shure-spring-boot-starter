package cn.shure.framework.security.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.shure.framework.security.annotation.handler.AnonymousApiMapping;
import cn.shure.framework.security.annotation.handler.ExposedApiMapping;
import cn.shure.framework.security.base.BaseAccessDecisionVoter;
import cn.shure.framework.security.base.ShureRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description BaseSecurityConfig
 * @Author 下课菌
 * @Date 2023/3/6
 */
@EnableWebSecurity
@Configuration
@Import({AnonymousApiMapping.class, ExposedApiMapping.class})
@ConfigurationProperties(prefix = "shure.security", ignoreUnknownFields = true)
@Slf4j
public class ShureSecurityConfig {

    @Resource
    private AnonymousApiMapping anonymousApiMapping;

    @Resource
    private ExposedApiMapping exposedApiMapping;

    @Autowired(required = false)
    private AccessDeniedHandler accessDeniedHandler;

    @Resource(shareable = false)
    private AuthenticationEntryPoint authenticationEntryPoint;

    private List<String> exposed;

    private List<String> anonymous;


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private void preprocessHttpSecurity(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        if (Objects.nonNull(authenticationEntryPoint)) {
            log.warn("AuthenticationEntryPoint缺失!");
            http.exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint);
        }
        if (Objects.nonNull(accessDeniedHandler)) {
            log.warn("AccessDeniedHandler缺失!");
            http.exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler);
        }
        anonymousApiMapping.forEach((httpMethod, patterns) -> {
            if (CollectionUtil.isNotEmpty(patterns)) {
                try {
                    http.authorizeRequests()
                            .antMatchers(httpMethod, patterns.toArray(new String[0]))
                            .anonymous();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        exposedApiMapping.forEach((httpMethod, patterns) -> {
            if (CollectionUtil.isNotEmpty(patterns)) {
                try {
                    http.authorizeRequests()
                            .antMatchers(httpMethod, patterns.toArray(new String[0]))
                            .permitAll();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Optional.ofNullable(exposed)
                .ifPresent(list -> {
                    if (CollectionUtil.isNotEmpty(list)) {
                        try {
                            http.authorizeRequests()
                                    .antMatchers(list.toArray(new String[0]))
                                    .permitAll();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        Optional.ofNullable(anonymous)
                .ifPresent(list -> {
                    if (CollectionUtil.isNotEmpty(list)) {
                        try {
                            http.authorizeRequests()
                                    .antMatchers(list.toArray(new String[0]))
                                    .anonymous();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private void addAuthenticationFilter(HttpSecurity http) {
        Map<String, ShureRequestFilter> authenticationFilterMap = SpringUtil.getBeansOfType(ShureRequestFilter.class);
        if (CollectionUtil.isEmpty(authenticationFilterMap)) {
            return;
        }
        ArrayList<ShureRequestFilter> filters = new ArrayList<>(authenticationFilterMap.values());
        filters.sort(Comparator.comparingInt(ShureRequestFilter::getOrder));
        ShureRequestFilter preFilter = filters.get(0);
        http.addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class);
        for (int index = 1; index < filters.size(); index++) {
            http.addFilterBefore(filters.get(index), preFilter.getClass());
            preFilter = filters.get(index);
        }
    }


    private void postprocessHttpSecurity(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        preprocessHttpSecurity(http);
        addAuthenticationFilter(http);
        postprocessHttpSecurity(http);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        Map<String, BaseAccessDecisionVoter> beansOfType = SpringUtil.getBeansOfType(BaseAccessDecisionVoter.class);
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new WebExpressionVoter());
        decisionVoters.addAll(beansOfType.values());
        return new UnanimousBased(decisionVoters);
    }

}
