package com.tnx.posBilling.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
// import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
        @Bean
        public OpenAPI myCustomConfig() {
                return new OpenAPI()
                                .info(
                                                new Info().title("PosBilling Application")
                                                                .description("By Technix"))
                                .servers(Arrays.asList(
                                                new Server().url("http://localhost:8080").description("local"),
                                                new Server().url("http://192.168.0.164:8080").description("live"),
                                                new Server().url("http://192.168.0.164:8888").description("working")))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .in(SecurityScheme.In.HEADER)
                                                                .name("Authorization")));
                // .addSchemas("MultipartFile", new Schema<>()
                // .type("string")
                // .format("binary")));
        }
}
