package co.invest72.exchange_rate.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.money.domain.Currency;
import co.invest72.money.domain.CurrencyPair;

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
		Assertions.assertThat(service.getRate(new CurrencyPair(from, to)).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(1000));
		Assertions.assertThat(service.getRate(new CurrencyPair(to, from)).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(0.001));
	}

	@DisplayName("환율 저장 - 원달러 환율이 1300원인 경우에 100만원은 769.23달러여야 한다")
	@Test
	void save_whenScaleIs4() {
		// given
		Currency from = Currency.dollar();
		Currency to = Currency.won();
		BigDecimal rate = BigDecimal.valueOf(1300);
		// when
		service.saveRate(from, to, rate);
		// then
		// 100만원 * (1/1300)원 = 769.23
		BigDecimal reverseRate = service.getRate(new CurrencyPair(to, from)).orElseThrow();
		BigDecimal sourceAmount = BigDecimal.valueOf(1_000_000);
		BigDecimal dollarResult = sourceAmount.multiply(reverseRate).setScale(2, RoundingMode.HALF_EVEN);
		Assertions.assertThat(dollarResult).isEqualByComparingTo(BigDecimal.valueOf(769.23));

		Assertions.assertThat(service.getRate(new CurrencyPair(from, to)).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(1300));
		Assertions.assertThat(service.getRate(new CurrencyPair(to, from)).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(0.0007692308));
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
		Assertions.assertThat(service.getRate(new CurrencyPair(from, to)).orElseThrow())
			.isEqualByComparingTo(BigDecimal.ZERO);
		Assertions.assertThat(service.getRate(new CurrencyPair(to, from))).isEmpty();
	}

	@DisplayName("환율 조회 - 원화 -> 달러에 대한 환율 조회")
	@Test
	void getRate_whenKRWToUSE_thenReturnRate() {
		// given
		Currency from = Currency.won();
		Currency to = Currency.dollar();
		service.saveRate(from, to, BigDecimal.valueOf(0.001));
		// when
		BigDecimal rate = service.getRate(new CurrencyPair(from, to)).orElseThrow();
		// then
		Assertions.assertThat(rate).isEqualByComparingTo(BigDecimal.valueOf(0.001));
	}

	@DisplayName("환율 조회 - 통화가 동일한 경우 1이 반환되어야 한다")
	@Test
	void getRate_whenKRWToKRW_thenReturnOne() {
		// given
		Currency won = Currency.won();
		// when
		BigDecimal rate = service.getRate(new CurrencyPair(won, won)).orElseThrow();
		// then
		Assertions.assertThat(rate).isEqualTo(BigDecimal.ONE);
	}
}
