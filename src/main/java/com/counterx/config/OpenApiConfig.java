package com.counterx.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BASIC_AUTH = "basicAuth";

    @Bean
    public OpenAPI counterxOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("CounterX API")
                        .description("Inventory and dashboard APIs for CounterX.")
                        .version("v1")
                        .contact(new Contact()
                                .name("CounterX"))
                        .license(new License()
                                .name("Internal use")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(BASIC_AUTH))
                .components(new Components()
                        .addSecuritySchemes(
                                BASIC_AUTH,
                                new SecurityScheme()
                                        .name(BASIC_AUTH)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));
    }
}
