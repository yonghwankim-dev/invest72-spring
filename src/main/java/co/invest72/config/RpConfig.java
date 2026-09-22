package co.invest72.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.rp.infrastructure.InMemoryRpRepository;
import co.invest72.rp.infrastructure.RpRepository;

@Configuration
public class RpConfig {
	@Bean
	public RpRepository rpRepository() {
		return new InMemoryRpRepository();
	}
}
