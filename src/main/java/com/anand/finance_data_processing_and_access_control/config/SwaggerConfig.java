package com.anand.finance_data_processing_and_access_control.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Finance Data Processing API",
                description = "Backend API for managing financial records with Role-Based Access Control.",
                version = "1.0",
                contact = @Contact(name = "Anand Bharti", email = "your.email@example.com")
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Enter your JWT token generated from the /api/v1/auth/login endpoint to access secured APIs."
)
public class SwaggerConfig {
    // No additional code needed here! The annotations do all the heavy lifting.
}