package com.sunish.weather.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Weather API",
                version = "1.0.0",
                description = "API for weather information",
                termsOfService = "http://swagger.io/terms/",
                contact = @Contact(
                        name = "Support Team",
                        email = "support@weather.com",
                        url = "http://weather.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://springdoc.org"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local server")
        }
)
public class OpenApiConfig {
    // Additional configuration if necessary
}
