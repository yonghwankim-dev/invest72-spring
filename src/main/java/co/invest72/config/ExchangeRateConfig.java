package co.invest72.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import co.invest72.exchange_rate.application.ExchangeRateUpdateHandler;
import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.KoreaeximClient;
import co.invest72.exchange_rate.domain.service.Bank;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.api.FixedExchangeRateProvider;
import co.invest72.exchange_rate.infrastructure.api.FixedKoreaeximClient;
import co.invest72.exchange_rate.infrastructure.persistence.InMemoryExchangeRateRepository;

@Configuration
public class ExchangeRateConfig {
	@Bean
	public Bank bank(ExchangeRateService exchangeRateService) {
		return new Bank(exchangeRateService);
	}

	@Bean
	public ExchangeRateRepository exchangeRateRepository() {
		return new InMemoryExchangeRateRepository();
	}

	@Bean
	public ExchangeRateService exchangeRateService() {
		return new ExchangeRateService();
	}

	@Bean
	@Profile(value = {"local", "test"})
	public KoreaeximClient koreaeximClient() {
		return new FixedKoreaeximClient();
	}

	@Bean
	@Profile(value = {"local", "test"})
	public FixedExchangeRateProvider fixedExchangeRateProvider(KoreaeximClient koreaeximClient,
		ExchangeRateUpdateHandler exchangeRateUpdateHandler) {
		return new FixedExchangeRateProvider(koreaeximClient, exchangeRateUpdateHandler);
	}
}
