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
		Currency expected = Currency.of("KRW");
		Assertions.assertThat(currency).isEqualTo(expected);
	}

	@DisplayName("통화 객체 생성 - null 값 전달시 예외가 발생해야 한다")
	@Test
	void newInstance_whenCurrencyIsNull_thenThrowException() {
		// when
		Throwable throwable = Assertions.catchThrowable(() -> Currency.of(null));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("통화 코드(code)는 null일 수 없습니다.");
	}

	@DisplayName("통화 생성 - 비어있는 문자열을 전달하면 예외가 발생해야 한다")
	@Test
	void newInstance_whenCurrencyIsEmpty_thenThrowException() {
		// when
		Throwable throwable = Assertions.catchThrowable(() -> Currency.of(""));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("통화 코드(code)는 빈 문자열일 수 없습니다.");
	}

}
