package cn.shure.framework.core.config;

import cn.shure.framework.core.annotation.handler.SingleBodyResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Description ShureWebMvcConfig
 * @Author 下课菌
 * @Date 2023/3/6
 */
public class ShureWebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SingleBodyResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
