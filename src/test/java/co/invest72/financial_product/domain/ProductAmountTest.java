package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductAmountTest {

	private void assertDoesNotThrowAnyExceptionForAmount(BigDecimal value) {
		Assertions.assertThatCode(() -> new ProductAmount(value))
			.doesNotThrowAnyException();
	}

	@DisplayName("금액이 0원 이상이어야 한다.")
	@Test
	void newInstance_whenAmountIsValid_thenCreateInstance() {
		assertDoesNotThrowAnyExceptionForAmount(BigDecimal.ZERO);
		assertDoesNotThrowAnyExceptionForAmount(new BigDecimal("0.01"));
		assertDoesNotThrowAnyExceptionForAmount(new BigDecimal("1000"));
		assertDoesNotThrowAnyExceptionForAmount(new BigDecimal("9999999999999.99"));
		assertDoesNotThrowAnyExceptionForAmount(new BigDecimal("10000000000000"));
	}
}
