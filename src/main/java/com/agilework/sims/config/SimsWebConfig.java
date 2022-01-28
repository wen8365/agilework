package com.agilework.sims.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SimsWebConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor newLoginInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(newLoginInterceptor())
                .excludePathPatterns("/login", "/adminRegister", "/error", "*.css", "*.js");
    }
}
