package com.couple.sns.common.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenApi() {

        String securitySchemeName = "bearerAuth";
        String apiTitle = "Couple-SNS";

        return new OpenAPI()
            .addServersItem(new Server().url("/"))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(
                new Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        new SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                        )
                )
            .info(new Info().title(apiTitle).version("0.1"));
    }
}
