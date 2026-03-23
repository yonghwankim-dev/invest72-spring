package co.invest72.financial_product.domain.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExpirationDateCalculatorTest {

	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		// when
		ExpirationDateCalculator calculator = new ExpirationDateCalculator();
		// then
		Assertions.assertThat(calculator).isNotNull();
	}

	@DisplayName("만기일 계산")
	@Test
	void calculate() {
		// given
		ExpirationDateCalculator calculator = new ExpirationDateCalculator();
		// when
		LocalDate expirationDate = calculator.calculate();
		// then
		Assertions.assertThat(expirationDate).isNotNull();
	}
}
