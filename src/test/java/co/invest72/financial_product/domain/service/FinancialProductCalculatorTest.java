package co.invest72.financial_product.domain.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.financial_product.domain.FinancialProduct;
import source.FinancialProductDataProvider;

class FinancialProductCalculatorTest {

	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		// when
		FinancialProductCalculator calculator = new FinancialProductCalculator();
		// then
		Assertions.assertThat(calculator).isNotNull();
	}

	@DisplayName("현금 상품의 만기일 계산")
	@Test
	void calculateExpirationDate() {
		// given
		FinancialProduct cash = FinancialProductDataProvider.createCashProduct("user-1234");
		FinancialProductCalculator calculator = new FinancialProductCalculator();
		// when
		LocalDate expirationDate = calculator.calculateExpirationDate(cash);
		// then
		Assertions.assertThat(expirationDate).isEqualTo(LocalDate.MAX);
	}

	@DisplayName("예금 상품의 만기일 계산")
	@Test
	void calculateExpirationDate_whenDeposit() {
		// given
		FinancialProduct product = FinancialProductDataProvider.createDepositProduct("user-1234");
		FinancialProductCalculator calculator = new FinancialProductCalculator();
		// when
		LocalDate expirationDate = calculator.calculateExpirationDate(product);
		// then
		Assertions.assertThat(expirationDate).isEqualTo(LocalDate.of(2027, 1, 1));
	}
}
