package com.techmojo.ratelimiting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.techmojo.ratelimiting.ratelimiterstratergy.RateLimitingInterceptor;

@Configuration
public class RateLimitingConfiguration implements WebMvcConfigurer{
	private RateLimitingInterceptor interceptor;

    public RateLimitingConfiguration(RateLimitingInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/api/**");
    }
}
