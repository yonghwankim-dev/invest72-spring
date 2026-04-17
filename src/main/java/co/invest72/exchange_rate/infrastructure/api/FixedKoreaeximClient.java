package co.invest72.exchange_rate.infrastructure.api;

import java.util.List;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.KoreaeximClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FixedKoreaeximClient implements KoreaeximClient {

	private final List<ExchangeJsonResponse> responses;

	public FixedKoreaeximClient() {
		Currency won = Currency.won();
		Currency dollar = Currency.dollar();
		responses = List.of(
			new ExchangeJsonResponse(1, won.getCode(), "1", won.getName()),
			new ExchangeJsonResponse(1, dollar.getCode(), "1000", dollar.getName())
		);
	}

	@Override
	public Flux<ExchangeJsonResponse> exchangeJson() {
		log.info("FixedExchangeRateProvider: 고정 환율 모드이므로 업데이트를 생략합니다.");
		return Flux.just(responses.toArray(ExchangeJsonResponse[]::new));
	}
}
