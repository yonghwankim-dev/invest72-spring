package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import source.FinancialProductDataProvider;

class FinancialProductTest {

	private FinancialProduct financialProduct;

	@DisplayName("현금 상품 진행률 계산 - 현금 상품은 진행률은 무조건 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenStartDateIsBeforeToday_thenReturnOne() {
		// Given
		financialProduct = FinancialProductDataProvider.createCashProduct("user-1");
		LocalDate today = LocalDate.of(2026, 1, 1).minusMonths(2);// 시작일을 오늘보다 2개월 이전으로 설정

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("예금 상품 진행률 계산 - 기준일자가 시작일자보다 이전인 경우 진행률은 0.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenStartDateIsAfterToday_thenReturnZero() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		// 시작일을 오늘보다 2개월 이전으로 설정
		LocalDate today = LocalDate.of(2026, 1, 1).minusMonths(2);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@DisplayName("예금 상품 진행률 계산 - 기준일자가 만기일 이후인 경우 진행률은 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsBeforeToday_thenReturnOne() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 2);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("예금 상품 진행률 계산 - 기준일자가 만기일과 동일한 경우 진행률은 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsEqualToToday_thenReturnOne() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 1);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("예금 상품 진행률 계산 - 기준일자가 만기일 이전인 경우 진행률은 0.0과 1.0 사이의 값이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsAfterToday_thenReturnBetweenZeroAndOne() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2026, 2, 27);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.valueOf(0.1562));
	}
}
