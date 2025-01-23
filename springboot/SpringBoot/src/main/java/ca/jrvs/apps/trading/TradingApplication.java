package ca.jrvs.apps.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = {"ca.jrvs.apps.trading", "ca.jrvs.apps.trading.model.config"})
public class TradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingApplication.class, args);
	}

}
