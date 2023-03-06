package cn.shure.framework.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @Description BaseShureConfig
 * @Author 下课菌
 * @Date 2023/3/6
 */
public interface BaseShureConfig {

    PasswordEncoder passwordEncoder();

    AuthenticationEntryPoint authenticationEntryPoint();

    AccessDeniedHandler accessDeniedHandler();

}
