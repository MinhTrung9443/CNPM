package com.cnpm.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebImgConfig implements WebMvcConfigurer{
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map "/uploads/**" to the physical directory where images are stored
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/WebImgUpload/");
	}
}
