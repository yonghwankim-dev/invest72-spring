package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import co.invest72.exchange_rate.application.ExchangeRateUpdateHandler;
import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.money.domain.Pair;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class KoreaeximExchangeRateProvider implements ExchangeRateProvider {
	private final KoreaeximClient client;
	private final ExchangeRateService exchangeRateService;
	private final ExchangeRateUpdateHandler exchangeRateUpdateHandler;

	public KoreaeximExchangeRateProvider(KoreaeximClient client, ExchangeRateService exchangeRateService,
		ExchangeRateUpdateHandler exchangeRateUpdateHandler) {
		this.client = Objects.requireNonNull(client);
		this.exchangeRateService = Objects.requireNonNull(exchangeRateService);
		this.exchangeRateUpdateHandler = Objects.requireNonNull(exchangeRateUpdateHandler);
	}

	@Override
	public Optional<BigDecimal> getRate(Currency from, Currency to) {
		return exchangeRateService.getRate(new Pair(from, to));
	}

	@Override
	public Flux<ExchangeJsonResponse> updateRates() {
		int success = 1;
		return client.exchangeJson()
			.filter(response -> response.getResult() == success)
			.flatMap(response ->
				Mono.fromRunnable(() -> exchangeRateUpdateHandler.handleUpdateRates(response))
					.thenReturn(response));
	}
}
