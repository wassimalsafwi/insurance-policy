package com.insurance.policy.adapter.in.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI insurancePolicyOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Insurance Policy API")
                        .description("API for managing insurance policies")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));

    }
}
