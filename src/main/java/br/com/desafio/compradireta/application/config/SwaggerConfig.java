package br.com.desafio.compradireta.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI compraDiretaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Compra Direta API")
                .description("Desafio técnico - Cadastro de Compra Direta")
                .version("1.0.0"));
    }
}
