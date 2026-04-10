package co.invest72.money.infrastructure.api;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.money.domain.Currency;
import co.invest72.money.domain.ExchangeRateProvider;

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

}
