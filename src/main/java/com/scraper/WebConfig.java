package com.scraper;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:4200")
            .allowedOrigins("https://panelinformativo.netlify.app/")
            .allowedOrigins("https://panel-informativo.web.app/")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
