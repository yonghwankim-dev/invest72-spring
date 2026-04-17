package co.invest72.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.service.Bank;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.api.FixedExchangeRateProvider;

@Configuration
public class MoneyConfig {
	@Bean
	public Bank bank(ExchangeRateService exchangeRateService) {
		return new Bank(exchangeRateService);
	}

	@Bean
	@Profile(value = {"local", "test"})
	public FixedExchangeRateProvider fixedExchangeRateProvider(ExchangeRateService exchangeRateService,
		ExchangeRateRepository exchangeRateRepository) {
		return new FixedExchangeRateProvider(exchangeRateService, exchangeRateRepository);
	}
}
