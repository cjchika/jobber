package com.cjchika.jobber.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI jobberOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Jobber API")
                        .version("1.0.0")
                        .description("API documentation for the jobber backend"));
    }
}
