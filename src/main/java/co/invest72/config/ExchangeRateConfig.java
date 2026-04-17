package co.invest72.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.persistence.InMemoryExchangeRateRepository;

@Configuration
public class ExchangeRateConfig {
	@Bean
	public ExchangeRateRepository exchangeRateRepository() {
		return new InMemoryExchangeRateRepository();
	}

	@Bean
	public ExchangeRateService exchangeRateService() {
		return new ExchangeRateService();
	}
}
