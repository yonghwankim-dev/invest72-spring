package co.invest72.financial_product.domain.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.financial_product.domain.FinancialProduct;
import source.FinancialProductDataProvider;

class ExpirationDateCalculatorTest {

	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		// when
		ExpirationDateCalculator calculator = new ExpirationDateCalculator();
		// then
		Assertions.assertThat(calculator).isNotNull();
	}

	@DisplayName("현금 상품의 만기일 계산")
	@Test
	void calculate() {
		// given
		FinancialProduct cash = FinancialProductDataProvider.createCashProduct("user-1234");
		ExpirationDateCalculator calculator = new ExpirationDateCalculator();
		// when
		LocalDate expirationDate = calculator.calculate(cash);
		// then
		Assertions.assertThat(expirationDate).isNotNull();
	}
}
