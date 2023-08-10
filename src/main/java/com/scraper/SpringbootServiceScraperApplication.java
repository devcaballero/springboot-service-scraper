package com.scraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class SpringbootServiceScraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServiceScraperApplication.class, args);

	}
	
	    @Bean
	    WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**")
	                        .allowedOrigins("https://panelinformativo.netlify.app", "https://panel-informativo.web.app")
	                        .allowedMethods("GET", "POST", "PUT")
	                        .allowedHeaders("*");
	            }
	        };
	    }
}