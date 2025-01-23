package ca.jrvs.apps.trading.model.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Jarvis Trading REST App")
                        .version("v1.0")
                        .description("API documentation for Jarvis Trading REST App"));
    }
}
