package co.invest72.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

import co.invest72.exchange_rate.application.ExchangeRateUpdateHandler;
import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.KoreaeximClient;
import co.invest72.exchange_rate.domain.service.Bank;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.api.FixedKoreaeximClient;
import co.invest72.exchange_rate.infrastructure.api.KoreaeximExchangeRateProvider;
import co.invest72.exchange_rate.infrastructure.api.KoreaeximProperties;
import co.invest72.exchange_rate.infrastructure.api.RealKoreaeximClient;

@Configuration
public class ExchangeRateConfig {
	@Bean
	public Bank bank(ExchangeRateService exchangeRateService) {
		return new Bank(exchangeRateService);
	}

	@Bean
	public ExchangeRateService exchangeRateService(ExchangeRateRepository repository) {
		return new ExchangeRateService(repository);
	}

	@Bean
	@Profile(value = {"local", "test"})
	public FixedKoreaeximClient koreaeximClient() {
		return new FixedKoreaeximClient();
	}

	@Bean
	@Profile(value = {"production"})
	public RealKoreaeximClient realKoreaeximClient(KoreaeximProperties properties) {
		WebClient webClient = WebClient.builder()
			.baseUrl(properties.getBaseUri())
			.build();
		return new RealKoreaeximClient(webClient, properties);
	}

	@Bean
	public KoreaeximExchangeRateProvider koreaeximExchangeRateProvider(KoreaeximClient koreaeximClient,
		ExchangeRateUpdateHandler handler) {
		return new KoreaeximExchangeRateProvider(koreaeximClient, handler);
	}
}
