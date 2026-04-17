package co.invest72.exchange_rate.infrastructure.api;

import java.util.Objects;

import co.invest72.exchange_rate.application.ExchangeRateUpdateHandler;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class KoreaeximExchangeRateProvider implements ExchangeRateProvider {
	private final RealKoreaeximClient client;
	private final ExchangeRateUpdateHandler exchangeRateUpdateHandler;

	public KoreaeximExchangeRateProvider(RealKoreaeximClient client,
		ExchangeRateUpdateHandler exchangeRateUpdateHandler) {
		this.client = Objects.requireNonNull(client);
		this.exchangeRateUpdateHandler = Objects.requireNonNull(exchangeRateUpdateHandler);
	}

	@Override
	public Flux<ExchangeJsonResponse> updateRates() {
		int success = 1;
		return client.exchangeJson()
			.filter(response -> response.getResult() == success)
			.flatMap(response ->
				Mono.fromRunnable(() -> exchangeRateUpdateHandler.handleUpdateRates(response))
					.subscribeOn(Schedulers.boundedElastic())
					.thenReturn(response));
	}
}
