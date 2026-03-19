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
		Currency expected = Currency.from("KRW");
		Assertions.assertThat(currency).isEqualTo(expected);
	}

	@DisplayName("통화 객체 생성 - null 값 전달시 예외가 발생해야 한다")
	@Test
	void newInstance_whenCurrencyIsNull_thenThrowException() {
		// when
		Throwable throwable = Assertions.catchThrowable(() -> Currency.from(null));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("잘못된 통화 코드(code) 입니다.");
	}

	@DisplayName("통화 생성 - 비어있는 문자열을 전달하면 예외가 발생해야 한다")
	@Test
	void newInstance_whenCurrencyIsEmpty_thenThrowException() {
		// when
		Throwable throwable = Assertions.catchThrowable(() -> Currency.from(""));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("잘못된 통화 코드(code) 입니다.");
	}

	@DisplayName("통화 생성 - 통화 코드는 3자리여야 한다")
	@Test
	void newInstance_whenCurrencyCodeIsNotThreeLetters_thenThrowException() {
		// when
		Throwable throwable = Assertions.catchThrowable(() -> Currency.from("US"));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("잘못된 통화 코드(code) 입니다.");
	}

	@DisplayName("통화 비교 - 동일한 통화 코드를 가진 객체들끼리는 동일한 객체로 간주")
	@Test
	void equals_whenSameCurrencyCode_thenReturnTrue() {
		// given
		Currency currency1 = Currency.from("USD");
		Currency currency2 = Currency.dollar();
		// when & then
		Assertions.assertThat(currency1)
			.hasSameHashCodeAs(currency2)
			.isEqualTo(currency2);
	}
}
