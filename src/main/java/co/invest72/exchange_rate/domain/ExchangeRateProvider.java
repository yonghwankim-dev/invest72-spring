package co.invest72.exchange_rate.domain;

import co.invest72.exchange_rate.infrastructure.api.ExchangeJsonResponse;
import reactor.core.publisher.Flux;

/**
 * 외부 환율 정보를 제공하는 인터페이스
 * <p>
 * 구현체는 수출입은행(koreaexim), 로컬 캐시, 고정 환율 등이 존재할 수 있다.
 * </p>
 */
public interface ExchangeRateProvider {
	/**
	 * 환율 정보를 최신 정보롤 업데이트한다.
	 */
	Flux<ExchangeJsonResponse> updateRates();
}

