package ca.jrvs.apps.trading.model.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alpha.vantage")
public class MarketDataConfig {
    private String host;
    private String token;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @PostConstruct
    public void logConfig() {
        System.out.println("Alpha Vantage Host: " + host);
        System.out.println("Alpha Vantage Token: " + token);
    }
}