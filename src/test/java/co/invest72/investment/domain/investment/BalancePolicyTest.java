package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.service.FinancialProductCalculator;
import source.FinancialProductDataProvider;

class BalancePolicyTest {

	private FinancialProductCalculator calculator;

	@BeforeEach
	void setUp() {
		calculator = new FinancialProductCalculator();
	}

	@DisplayName("투자 금액 계산 - FIXED, 기준일자 상관없이 원금을 반환한다")
	@Test
	void calculate_fixed_beforeStartDate() {
		// Given
		BalancePolicy policy = BalancePolicy.FIXED;
		FinancialProduct product = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2026, 1, 1);
		LocalDate expirationDate = calculator.calculateExpirationDate(product);

		// When
		BigDecimal balance = policy.calculate(product, today, expirationDate);

		// Then
		Assertions.assertThat(balance)
			.isEqualByComparingTo(product.getAmount().getValue());
	}

	@DisplayName("투자 금액 계산 - ACCUMULATIVE, 기준일자가 시작일자 하루전인 경우 투자 금액은 0이다")
	@Test
	void calculate_accumulative_beforeStartDate() {
		// Given
		BalancePolicy policy = BalancePolicy.ACCUMULATIVE;
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2025, 12, 31);
		LocalDate expirationDate = calculator.calculateExpirationDate(product);

		// When
		BigDecimal balance = policy.calculate(product, today, expirationDate);

		// Then
		Assertions.assertThat(balance)
			.isEqualByComparingTo(BigDecimal.ZERO);
	}

	@DisplayName("투자 금액 계산 - ACCUMULATIVE, 기준일자가 만기일자 하루후인 경우 투자 금액은 원금 * 개월수이다")
	@Test
	void calculate_accumulative_afterExpirationDate() {
		// Given
		BalancePolicy policy = BalancePolicy.ACCUMULATIVE;
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 2);
		LocalDate expirationDate = calculator.calculateExpirationDate(product);

		// When
		BigDecimal balance = policy.calculate(product, today, expirationDate);

		// Then
		BigDecimal expectedBalance = product.getAmount().getValue()
			.multiply(BigDecimal.valueOf(product.getMonths().getValue()));
		Assertions.assertThat(balance)
			.isEqualByComparingTo(expectedBalance);
	}
}
