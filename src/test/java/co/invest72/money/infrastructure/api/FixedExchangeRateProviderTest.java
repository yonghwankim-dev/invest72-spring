package co.invest72.money.infrastructure.api;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.money.domain.Currency;
import co.invest72.money.domain.ExchangeRateProvider;
import co.invest72.money.domain.Pair;

class FixedExchangeRateProviderTest {

	private ExchangeRateProvider exchangeRateProvider;

	@BeforeEach
	void setUp() {
		exchangeRateProvider = new FixedExchangeRateProvider();
	}

	@DisplayName("환율 조회 - 원화 -> 달러에 대한 환율 조회")
	@Test
	void getRate_whenKRWToUSE_thenReturnRate() {
		// given
		Currency won = Currency.won();
		Currency dollar = Currency.dollar();
		// when
		BigDecimal rate = exchangeRateProvider.getRate(won, dollar);
		// then
		Assertions.assertThat(rate).isEqualTo(BigDecimal.valueOf(0.001));
	}

	@DisplayName("환율 조회 - 통화가 동일한 경우 1이 반환되어야 한다")
	@Test
	void getRate_whenKRWToKRW_thenReturnOne() {
		// given
		Currency won = Currency.won();
		Currency won2 = Currency.won();
		// when
		BigDecimal rate = exchangeRateProvider.getRate(won, won2);
		// then
		Assertions.assertThat(rate).isEqualTo(BigDecimal.ONE);
	}

	@DisplayName("환전 초기화 - 저장된 환율 정보를 초기화한다")
	@Test
	void clear() {
		// given
		Pair pair = new Pair(Currency.won(), Currency.dollar());
		exchangeRateProvider.addRate(pair, BigDecimal.valueOf(0.001));
		// when
		exchangeRateProvider.clear();
		// then
		BigDecimal rate = exchangeRateProvider.getRate(Currency.won(), Currency.dollar());
		Assertions.assertThat(rate).isNull();
	}
}
