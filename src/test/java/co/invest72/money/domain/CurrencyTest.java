package co.invest72.money.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CurrencyTest {

	@DisplayName("통화 객체 생성 - 원화 생성")
	@Test
	void newInstance_whenCurrencyIsKRW_thenReturnCurrency() {
		// when
		Currency currency = Currency.won();
		// then
		Currency expected = Currency.won();
		Assertions.assertThat(currency).isEqualTo(expected);
	}

	@DisplayName("통화 비교 - 동일한 통화 코드를 가진 객체들끼리는 동일한 객체로 간주")
	@Test
	void equals_whenSameCurrencyCode_thenReturnTrue() {
		// given
		Currency currency1 = Currency.dollar();
		Currency currency2 = Currency.dollar();
		// when & then
		Assertions.assertThat(currency1)
			.hasSameHashCodeAs(currency2)
			.isEqualTo(currency2);
	}
}
