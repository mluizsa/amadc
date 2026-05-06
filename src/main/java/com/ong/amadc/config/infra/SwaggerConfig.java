package com.ong.amadc.config.infra;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AMADC API - Core Engine")
                        .version("1.0")
                        .description("Documentação da API para o projeto AMADC, ONG de Animais."))
                .servers(List.of(new Server().url("http://localhost:8080").description("Servidor Local")));
    }
}
