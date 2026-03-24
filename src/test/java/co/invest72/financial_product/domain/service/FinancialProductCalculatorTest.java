package co.invest72.financial_product.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.financial_product.domain.FinancialProduct;
import source.FinancialProductDataProvider;

class FinancialProductCalculatorTest {

	private FinancialProductCalculator calculator;

	@BeforeEach
	void setUp() {
		calculator = new FinancialProductCalculator();
	}

	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		// when
		FinancialProductCalculator calculator = new FinancialProductCalculator();
		// then
		Assertions.assertThat(calculator).isNotNull();
	}

	@DisplayName("현금 상품 만기일 계산 - 현금 상품은 만기일이 LocalDate.MAX로 설정된다.")
	@Test
	void calculateExpirationDate_whenCashProduct_thenReturnLocalDateMax() {
		// Given
		FinancialProduct financialProduct = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProductCalculator calculator = new FinancialProductCalculator();

		// When
		LocalDate expirationDate = calculator.calculateExpirationDate(financialProduct);

		// Then
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

	@DisplayName("예금 상품의 잔고 계산")
	@Test
	void calculateBalance_whenDeposit() {
		// given
		FinancialProduct product = FinancialProductDataProvider.createDepositProduct("user-1234");
		LocalDate today = LocalDate.of(2026, 2, 1);
		// when
		BigDecimal balance = calculator.calculateBalance(product, today);
		// then
		Assertions.assertThat(balance).isEqualTo(BigDecimal.valueOf(1_000_000L));
	}

	@DisplayName("현금 상품 진행률 계산 - 현금 상품은 진행률은 무조건 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenStartDateIsBeforeToday_thenReturnOne() {
		// Given
		FinancialProduct product = FinancialProductDataProvider.createCashProduct("user-1");
		LocalDate today = LocalDate.of(2026, 1, 1).minusMonths(2);// 시작일을 오늘보다 2개월 이전으로 설정

		// When
		BigDecimal progress = calculator.calculateProgress(product, today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}
}
