package co.invest72.exchange_rate.domain;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 외부 환율 정보를 제공하는 인터페이스
 * <p>
 * 구현체는 수출입은행(koreaexim), 로컬 캐시, 고정 환율 등이 존재할 수 있다.
 * </p>
 */
public interface ExchangeRateProvider {
	/**
	 * 특정 통화간의 환율(rate)을 조회한다.
	 *
	 * @param from 원본 통화
	 * @param to 대상 통화
	 * @return 환율 값(BigDecimal), 존재하지 않을 경우 empty 반환.
	 */
	Optional<BigDecimal> getRate(Currency from, Currency to);
}
