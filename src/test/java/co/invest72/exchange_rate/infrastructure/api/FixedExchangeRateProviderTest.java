package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.money.domain.Currency;

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
		BigDecimal rate = exchangeRateProvider.getRate(won, dollar).orElseThrow();
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
		BigDecimal rate = exchangeRateProvider.getRate(won, won2).orElseThrow();
		// then
		Assertions.assertThat(rate).isEqualTo(BigDecimal.ONE);
	}
}
