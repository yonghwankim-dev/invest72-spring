package co.invest72.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.exchange_rate.domain.service.Bank;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;

@Configuration
public class MoneyConfig {
	@Bean
	public Bank bank(ExchangeRateService exchangeRateService) {
		return new Bank(exchangeRateService);
	}
}
