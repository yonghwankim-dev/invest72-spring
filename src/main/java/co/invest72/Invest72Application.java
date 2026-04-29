package co.invest72;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan("co.invest72")
@EnableScheduling
public class Invest72Application {
	public static void main(String[] args) {
		SpringApplication.run(Invest72Application.class, args);
	}

}
