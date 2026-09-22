package ar.db.projeto_votacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig{

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
                new Info()
                        .title("Votação API")
                        .description("API REST do sistema de votação")
                        .contact(
                    new Contact()
                             .name("Cristian Tiago Dutkercz Rosa")
                             .email("cristian.rosa@...")
                             .url("https://github.com/Dutkercz/desafio-votacao-fullstack")));


    }
}
