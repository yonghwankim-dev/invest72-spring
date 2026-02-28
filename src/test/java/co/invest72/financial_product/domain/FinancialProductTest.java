package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import source.FinancialProductDataProvider;

class FinancialProductTest {

	private FinancialProduct financialProduct;

	@DisplayName("현금 상품 만기일 계산 - 현금 상품은 만기일이 LocalDate.MAX로 설정된다.")
	@Test
	void calculateExpirationDate_whenCashProduct_thenReturnLocalDateMax() {
		// Given
		financialProduct = FinancialProductDataProvider.createCashProduct("user-1");

		// When
		LocalDate expirationDate = financialProduct.getExpirationDate();

		// Then
		Assertions.assertThat(expirationDate).isEqualTo(LocalDate.MAX);
	}

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

	@DisplayName("현금 상품 남은 일수 계산 - 현금 상품은 남은 일수가 항상 0이 반환된다.")
	@Test
	void getRemainingDaysByLocalDate_whenCashProduct_thenReturnZero() {
		// Given
		financialProduct = FinancialProductDataProvider.createCashProduct("user-1");
		LocalDate today = LocalDate.of(2026, 1, 1);

		// When
		long remainingDays = financialProduct.getRemainingDaysByLocalDate(today);

		// Then
		Assertions.assertThat(remainingDays).isZero();
	}

	@DisplayName("현금 상품 현재 잔액 계산 - 현금 상품은 언제든지 잔액이 원금이 반환된다.")
	@Test
	void getBalanceByLocalDate_whenCashProduct_thenReturnPrincipal() {
		// Given
		financialProduct = FinancialProductDataProvider.createCashProduct("user-1");
		LocalDate today = LocalDate.of(2026, 1, 1).minusMonths(2); // 시작일을 오늘보다 2개월 이전으로 설정

		// When
		BigDecimal balance = financialProduct.getBalanceByLocalDate(today);

		// Then
		Assertions.assertThat(balance).isEqualByComparingTo(BigDecimal.valueOf(1_000_000L));
	}

	@DisplayName("예금 상품 만기일 계산 - 시작일자로부터 설정된 개월 수만큼 더한 날짜가 만기일로 계산된다.")
	@Test
	void calculateExpirationDate_whenDepositProduct_thenReturnCorrectExpirationDate() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate expectedExpirationDate = LocalDate.of(2027, 1, 1);

		// When
		LocalDate expirationDate = financialProduct.getExpirationDate();

		// Then
		Assertions.assertThat(expirationDate).isEqualTo(expectedExpirationDate);
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

	@DisplayName("예금 상품 남은 일수 계산 - 기준일자가 만기일 이후인 경우 남은 일수는 0이 반환된다.")
	@Test
	void getRemainingDaysByLocalDate_whenExpirationDateIsBeforeToday_thenReturnZero() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 2);

		// When
		long remainingDays = financialProduct.getRemainingDaysByLocalDate(today);

		// Then
		Assertions.assertThat(remainingDays).isZero();
	}

	@DisplayName("예금 상품 남은 일수 계산 - 기준일자가 만기일과 동일한 경우 남은 일수는 0이 반환된다.")
	@Test
	void getRemainingDaysByLocalDate_whenExpirationDateIsEqualToToday_thenReturnZero() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 1);

		// When
		long remainingDays = financialProduct.getRemainingDaysByLocalDate(today);

		// Then
		Assertions.assertThat(remainingDays).isZero();
	}

	@DisplayName("예금 상품 남은 일수 계산 - 기준일자가 만기일 이전인 경우 남은 일수는 0보다 큰 값이 반환된다.")
	@Test
	void getRemainingDaysByLocalDate_whenExpirationDateIsAfterToday_thenReturnPositiveValue() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		LocalDate today = LocalDate.of(2026, 2, 27);

		// When
		long remainingDays = financialProduct.getRemainingDaysByLocalDate(today);

		// Then
		Assertions.assertThat(remainingDays).isGreaterThan(0);
	}

	@DisplayName("예금 상품 현재 잔액 계산 - 기준일자가 언제든지 잔액은 원금이 반환된다.")
	@Test
	void getBalanceByLocalDate_whenStartDateIsAfterToday_thenReturnZeroForDeposit() {
		// Given
		financialProduct = FinancialProductDataProvider.createDepositProduct("user-1");
		// 시작일을 오늘보다 2개월 이전으로 설정
		LocalDate today = LocalDate.of(2026, 1, 1).minusMonths(2);

		// When
		BigDecimal balance = financialProduct.getBalanceByLocalDate(today);

		// Then
		Assertions.assertThat(balance).isEqualByComparingTo(BigDecimal.valueOf(1_000_000L));
	}

	@DisplayName("적금 상품 만기일 계산 - 시작일자로부터 설정된 개월 수만큼 더한 날짜가 만기일로 계산된다.")
	@Test
	void calculateExpirationDate_whenSavingsProduct_thenReturnCorrectExpirationDate() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate expectedExpirationDate = LocalDate.of(2027, 1, 1);

		// When
		LocalDate expirationDate = financialProduct.getExpirationDate();

		// Then
		Assertions.assertThat(expirationDate).isEqualTo(expectedExpirationDate);
	}

	@DisplayName("적금 상품 진행률 계산 - 기준일자가 시작일자보다 이전인 경우 진행률은 0.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenStartDateIsAfterToday_thenReturnZeroForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		// 시작일을 오늘보다 2개월 이전으로 설정
		LocalDate today = LocalDate.of(2026, 1, 1).minusMonths(2);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@DisplayName("적금 상품 진행률 계산 - 기준일자가 만기일 이후인 경우 진행률은 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsBeforeToday_thenReturnOneForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 2);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("적금 상품 진행률 계산 - 기준일자가 만기일과 동일한 경우 진행률은 1.0이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsEqualToToday_thenReturnOneForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 1);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.ONE);
	}

	@DisplayName("적금 상품 진행률 계산 - 기준일자가 만기일 이전인 경우 진행률은 0.0과 1.0 사이의 값이 반환된다.")
	@Test
	void getProgressByLocalDate_whenExpirationDateIsAfterToday_thenReturnBetweenZeroAndOneForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2026, 2, 27);

		// When
		BigDecimal progress = financialProduct.getProgressByLocalDate(today);

		// Then
		Assertions.assertThat(progress).isEqualByComparingTo(BigDecimal.valueOf(0.1562));
	}

	@DisplayName("적금 상품 현재 잔액 계산 - 기준일자가 시작일자보다 이전인 경우 잔액은 0이 반환된다.")
	@Test
	void getBalanceByLocalDate_whenStartDateIsAfterToday_thenReturnZeroForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		// 시작일을 오늘보다 2개월 이전으로 설정
		LocalDate today = LocalDate.of(2026, 1, 1).minusMonths(2);

		// When
		BigDecimal balance = financialProduct.getBalanceByLocalDate(today);

		// Then
		Assertions.assertThat(balance).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@DisplayName("적금 상품 현재 잔액 계산 - 기준일자가 만기일 이후인 경우 잔액은 월 적립액 * 총 개월 수가 반환된다.")
	@Test
	void getBalanceByLocalDate_whenExpirationDateIsBeforeToday_thenReturnFullBalanceForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 2);
		BigDecimal expectedBalance = BigDecimal.valueOf(1_000_000L).multiply(BigDecimal.valueOf(12));

		// When
		BigDecimal balance = financialProduct.getBalanceByLocalDate(today);

		// Then
		Assertions.assertThat(balance).isEqualByComparingTo(expectedBalance);
	}

	@DisplayName("적금 상품 현재 잔액 계산 - 기준일자가 만기일과 동일한 경우 잔액은 월 적립액 * 총 개월 수가 반환된다.")
	@Test
	void getBalanceByLocalDate_whenExpirationDateIsEqualToToday_thenReturnFullBalanceForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2027, 1, 1);
		BigDecimal expectedBalance = BigDecimal.valueOf(1_000_000L).multiply(BigDecimal.valueOf(12));

		// When
		BigDecimal balance = financialProduct.getBalanceByLocalDate(today);

		// Then
		Assertions.assertThat(balance).isEqualByComparingTo(expectedBalance);
	}

	@DisplayName("적금 상품 현재 잔액 계산 - 기준일자가 만기일 이전인 경우 잔액은 월 적립액 * 경과 개월 수가 반환된다.")
	@Test
	void getBalanceByLocalDate_whenExpirationDateIsAfterToday_thenReturnPartialBalanceForSavings() {
		// Given
		financialProduct = FinancialProductDataProvider.createSavingsProduct("user-1");
		LocalDate today = LocalDate.of(2026, 2, 27);
		BigDecimal expectedBalance = BigDecimal.valueOf(1_000_000L).multiply(BigDecimal.valueOf(1)); // 1개월 경과

		// When
		BigDecimal balance = financialProduct.getBalanceByLocalDate(today);

		// Then
		Assertions.assertThat(balance).isEqualByComparingTo(expectedBalance);
	}
}
