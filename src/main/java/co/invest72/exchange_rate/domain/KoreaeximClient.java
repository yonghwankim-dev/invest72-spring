package co.invest72.exchange_rate.domain;

import co.invest72.exchange_rate.infrastructure.api.ExchangeJsonResponse;
import reactor.core.publisher.Flux;

public interface KoreaeximClient {
	Flux<ExchangeJsonResponse> exchangeJson();
}
