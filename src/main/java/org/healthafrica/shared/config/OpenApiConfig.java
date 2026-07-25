package org.healthafrica.shared.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration for HealthAfrica API documentation.
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH = "bearerAuth";
    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Bean
    OpenAPI healthAfricaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("HealthAfrica API")
                        .description("""
                                Public Health Engagement & Data Portal REST API.

                                Most endpoints require a Bearer JWT and the `X-Tenant-ID` header.
                                Authenticate via `POST /api/auth/login` with the tenant header set to
                                the organization subdomain (for example `NGO_A` or `PLATFORM`).
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("HealthAfrica")
                                .url("https://github.com"))
                        .license(new License()
                                .name("Open Source")
                                .url("https://opensource.org/licenses")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(BEARER_AUTH)
                        .addList(TENANT_HEADER))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT obtained from POST /api/auth/login"))
                        .addSecuritySchemes(TENANT_HEADER, new SecurityScheme()
                                .name(TENANT_HEADER)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("Tenant / organization subdomain code"))
                        .addParameters(TENANT_HEADER, new Parameter()
                                .in(ParameterIn.HEADER.toString())
                                .name(TENANT_HEADER)
                                .required(true)
                                .description("Tenant / organization subdomain code")
                                .schema(new StringSchema().example("NGO_A"))));
    }
}
