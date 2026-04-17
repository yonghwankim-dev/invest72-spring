package co.invest72.exchange_rate.infrastructure.api;

import java.util.Objects;

import co.invest72.exchange_rate.application.ExchangeRateUpdateHandler;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.exchange_rate.domain.KoreaeximClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class FixedExchangeRateProvider implements ExchangeRateProvider {
	private final KoreaeximClient koreaeximClient;
	private final ExchangeRateUpdateHandler exchangeRateUpdateHandler;

	public FixedExchangeRateProvider(KoreaeximClient koreaeximClient,
		ExchangeRateUpdateHandler exchangeRateUpdateHandler) {
		this.koreaeximClient = Objects.requireNonNull(koreaeximClient);
		this.exchangeRateUpdateHandler = Objects.requireNonNull(exchangeRateUpdateHandler);
	}

	@Override
	public Flux<ExchangeJsonResponse> updateRates() {
		int success = 1;
		return koreaeximClient.exchangeJson()
			.filter(response -> response.getResult() == success)
			.flatMap(response -> Mono.fromRunnable(() -> exchangeRateUpdateHandler.handleUpdateRates(response))
				.subscribeOn(Schedulers.boundedElastic())
				.thenReturn(response));
	}
}
