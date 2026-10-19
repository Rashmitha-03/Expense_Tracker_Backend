package com.expensetracker.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        // ✅ Define JWT Security Scheme
        SecurityScheme jwtScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // ✅ Add Security Requirement (Authorize button)
        SecurityRequirement requirement =
                new SecurityRequirement().addList("BearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("Expense Tracker API")
                        .version("1.0")
                        .description("Swagger Documentation for Expense Tracker with JWT Authentication"))

                // ✅ Add JWT scheme into Swagger UI
                .schemaRequirement("BearerAuth", jwtScheme)

                // ✅ Apply JWT globally to all endpoints
                .addSecurityItem(requirement);
    }
}
