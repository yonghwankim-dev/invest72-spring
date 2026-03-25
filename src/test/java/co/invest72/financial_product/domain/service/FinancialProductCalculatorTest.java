package co.invest72.financial_product.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import co.invest72.financial_product.domain.FinancialProduct;

class FinancialProductCalculatorTest {

	private FinancialProductCalculator calculator;

	@BeforeEach
	void setUp() {
		calculator = new FinancialProductCalculator();
	}

	@DisplayName("금융 상품 만기일 계산")
	@ParameterizedTest(name = "desc={2}")
	@MethodSource(value = {"source.FinancialProductSourceProvider#provideExpirationSource"})
	void calculateExpirationDate_whenCashProduct_thenReturnLocalDateMax(FinancialProduct product, LocalDate expected,
		String ignored) {
		// When
		LocalDate expirationDate = calculator.calculateExpirationDate(product);
		// Then
		Assertions.assertThat(expirationDate).isEqualTo(expected);
	}

	@DisplayName("금융 상품의 잔고 계산")
	@ParameterizedTest(name = "today={1}, desc={3}")
	@MethodSource(value = {"source.FinancialProductSourceProvider#provideCashBalanceSource",
		"source.FinancialProductSourceProvider#provideDepositBalanceSource",
		"source.FinancialProductSourceProvider#provideSavingsBalanceSource"
	})
	void givenProductAndLocalDate_whenCalculateBalance_thenReturnBalance(FinancialProduct product, LocalDate today,
		BigDecimal expected, String ignored) {
		// When
		BigDecimal balance = calculator.calculateBalance(product, today);
		// Then
		Assertions.assertThat(balance).isEqualTo(expected);
	}

	@DisplayName("금융 상품의 진행률 남은 일수 계산")
	@ParameterizedTest(name = "today={1}, desc={3}")
	@MethodSource(value = "source.FinancialProductSourceProvider#provideRemainingDaysSource")
	void givenProductAndToday_whenCalculateRemainingDays_thenReturnLong(FinancialProduct product, LocalDate today,
		Long expected, String ignored) {
		// When
		long remainingDays = calculator.calculateRemainingDays(product, today);
		// Then
		Assertions.assertThat(remainingDays).isEqualTo(expected);
	}

	@DisplayName("금융 상품 진행률 계산")
	@ParameterizedTest(name = "today={1}, desc={3}")
	@MethodSource(value = "source.FinancialProductSourceProvider#provideProgressSource")
	void givenProductAndToday_whenCalculateProgress_thenReturnProgress(FinancialProduct product, LocalDate today,
		BigDecimal expected, String ignored) {
		// When
		BigDecimal progress = calculator.calculateProgress(product, today);
		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(expected);
	}
}
