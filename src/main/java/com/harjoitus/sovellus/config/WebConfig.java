package com.harjoitus.sovellus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Setup the resource handler for static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/public/", "classpath:/resources/")
                .setCachePeriod(3600)
                .resourceChain(true); // Enable the resource chain for efficient resolution of static resources
    }
}
