package co.invest72.exchange_rate.domain.service;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.money.domain.Pair;

class ExchangeRateServiceTest {

	private ExchangeRateService service;

	@BeforeEach
	void setUp() {
		service = new ExchangeRateService();
	}

	@DisplayName("환율 저장")
	@Test
	void save() {
		// given
		Currency from = Currency.dollar();
		Currency to = Currency.won();
		BigDecimal rate = BigDecimal.valueOf(1000);
		// when
		service.saveRate(from, to, rate);
		// then
		Assertions.assertThat(service.getRate(new Pair(from, to)).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(1000));
		Assertions.assertThat(service.getRate(new Pair(to, from)).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(0.001));
	}

	@DisplayName("환율 저장 - 환율이 양수가 아니면 역방향 환율이 저장되지 않는다")
	@Test
	void save_whenRateIsNotPositive_thenNotSavedReverseRate() {
		// given
		Currency from = Currency.dollar();
		Currency to = Currency.won();
		BigDecimal rate = BigDecimal.ZERO;
		// when
		service.saveRate(from, to, rate);
		// then
		Assertions.assertThat(service.getRate(new Pair(from, to)).orElseThrow())
			.isEqualByComparingTo(BigDecimal.ZERO);
		Assertions.assertThat(service.getRate(new Pair(to, from))).isEmpty();
	}
}
