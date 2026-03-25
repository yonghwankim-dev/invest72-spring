package co.invest72.financial_product.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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
		calculator = new FinancialProductCalculator();
		// then
		Assertions.assertThat(calculator).isNotNull();
	}

	@DisplayName("금융 상품 만기일 계산")
	@ParameterizedTest(name = "만기일 계산: desc={2}")
	@MethodSource(value = {"source.FinancialProductBalanceSourceProvider#provideExpirationSource"})
	void calculateExpirationDate_whenCashProduct_thenReturnLocalDateMax(FinancialProduct product, LocalDate expected,
		String ignored) {
		// When
		LocalDate expirationDate = calculator.calculateExpirationDate(product);
		// Then
		Assertions.assertThat(expirationDate).isEqualTo(expected);
	}
	
	@DisplayName("금융 상품의 잔고 계산")
	@ParameterizedTest(name = "잔액 계산: today={1}, desc={3}")
	@MethodSource(value = {"source.FinancialProductBalanceSourceProvider#provideCashBalanceSource",
		"source.FinancialProductBalanceSourceProvider#provideDepositBalanceSource",
		"source.FinancialProductBalanceSourceProvider#provideSavingsBalanceSource"
	})
	void givenProductAndLocalDate_whenCalculateBalance_thenReturnBalance(FinancialProduct product, LocalDate today,
		BigDecimal expected, String ignored) {
		// When
		BigDecimal balance = calculator.calculateBalance(product, today);
		// Then
		Assertions.assertThat(balance).isEqualTo(expected);
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
