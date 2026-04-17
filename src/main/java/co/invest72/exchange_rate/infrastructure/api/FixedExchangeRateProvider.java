package co.invest72.exchange_rate.infrastructure.api;

import java.util.Objects;

import co.invest72.exchange_rate.application.ExchangeRateUpdateHandler;
import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class FixedExchangeRateProvider implements ExchangeRateProvider {
	private final ExchangeRateService exchangeRateService;
	private final ExchangeRateUpdateHandler exchangeRateUpdateHandler;

	public FixedExchangeRateProvider(ExchangeRateService exchangeRateService,
		ExchangeRateUpdateHandler exchangeRateUpdateHandler) {
		this.exchangeRateService = Objects.requireNonNull(exchangeRateService);
		this.exchangeRateUpdateHandler = Objects.requireNonNull(exchangeRateUpdateHandler);
	}

	@Override
	public Flux<ExchangeJsonResponse> updateRates() {
		log.info("FixedExchangeRateProvider: 고정 환율 모드이므로 업데이트를 생략합니다.");
		Currency won = Currency.won();
		Currency dollar = Currency.dollar();
		int success = 1;
		return Flux.just(
				new ExchangeJsonResponse(1, won.getCode(), "1", won.getName()),
				new ExchangeJsonResponse(1, dollar.getCode(), "1000", dollar.getName())
			).filter(response -> response.getResult() == success)
			.flatMap(response -> Mono.fromRunnable(() -> exchangeRateUpdateHandler.handleUpdateRates(response))
				.subscribeOn(Schedulers.boundedElastic())
				.thenReturn(response));
	}
}
