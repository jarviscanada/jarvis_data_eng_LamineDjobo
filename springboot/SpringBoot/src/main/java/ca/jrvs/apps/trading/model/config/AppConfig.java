package ca.jrvs.apps.trading.model.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CloseableHttpClient closeableHttpClient() {
        // Crée un client HTTP prêt à être utilisé
        return HttpClients.createDefault();
    }
}

