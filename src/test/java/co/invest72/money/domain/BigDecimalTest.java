package co.invest72.money.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BigDecimalTest {
	@DisplayName("비교 - 비교 대상이 null인 경우 예외가 발생해야 한다")
	@Test
	void compareTo_whenOtherIsNull_thenThrowException() {
		// given
		BigDecimal other = null;
		// when
		Throwable throwable = Assertions.catchThrowable(() -> BigDecimal.ZERO.compareTo(other));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(NullPointerException.class);
	}
}
