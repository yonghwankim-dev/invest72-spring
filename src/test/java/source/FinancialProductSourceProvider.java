package source;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import co.invest72.financial_product.domain.FinancialProduct;

public class FinancialProductSourceProvider {

	public static Stream<Arguments> provideExpirationSource() {
		FinancialProduct cash = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct deposit = FinancialProductDataProvider.createDepositProduct("user-1");
		FinancialProduct savings = FinancialProductDataProvider.createSavingsProduct("user-1");
		return Stream.of(
			Arguments.of(cash, LocalDate.MAX, "현금 상품: 만기일이 LocalDate.MAX여야 한다"),
			Arguments.of(deposit, LocalDate.of(2027, 1, 1), "예금 상품"),
			Arguments.of(savings, LocalDate.of(2027, 1, 1), "적금 상품")
		);
	}

	public static Stream<Arguments> provideCashBalanceSource() {
		FinancialProduct product = FinancialProductDataProvider.createCashProduct("user-1");
		BigDecimal expected = product.getAmount().getValue();
		return Stream.of(
			Arguments.of(product, LocalDate.of(2026, 1, 1), expected, "현금 상품: 기준일자가 시작일자와 동일한 경우"),
			Arguments.of(product, LocalDate.of(2026, 1, 1).minusDays(1), expected, "현금 상품: 기준일자가 시작일자보다 이전인 경우"),
			Arguments.of(product, LocalDate.of(2026, 1, 1).plusMonths(12), expected, "현금 상품: 기준일자가 시작일자보다 이후인 경우")
		);
	}

	public static Stream<Arguments> provideDepositBalanceSource() {
		FinancialProduct product = FinancialProductDataProvider.createDepositProduct("user-1");
		BigDecimal expected = product.getAmount().getValue();
		return Stream.of(
			Arguments.of(product, LocalDate.of(2026, 1, 1), expected, "예금 상품: 기준일자가 시작일자와 동일한 경우"),
			Arguments.of(product, LocalDate.of(2026, 1, 1).minusDays(1), expected, "예금 상품: 기준일자가 시작일자보다 이전인 경우"),
			Arguments.of(product, LocalDate.of(2026, 1, 1).plusMonths(12), expected, "예금 상품: 기준일자가 시작일자보다 이후인 경우")
		);
	}

	public static Stream<Arguments> provideSavingsBalanceSource() {
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct("user-1");
		BigDecimal unit = BigDecimal.valueOf(1_000_000);
		return Stream.of(
			// 기준일자가 시작일 이전
			Arguments.of(product, LocalDate.of(2026, 1, 1).minusMonths(2), BigDecimal.ZERO,
				"2025년 11월 1일: 초회 납입 시작 안함 (총 0원)"),

			// 가입 당일 ~ 1월 전체(초회 납입 1회만 인정)
			Arguments.of(product, LocalDate.of(2026, 1, 1), unit, "가입 당일: 초회납입 100만 적립"),
			Arguments.of(product, LocalDate.of(2026, 1, 15), unit, "1월 15일: 가입일 정기납입은 스킵되므로 여전히 100만원"),
			Arguments.of(product, LocalDate.of(2026, 1, 31), unit, "1월 31일: 여전히 100만원"),
			// 2월 정기 납입 (첫 정기납입 시작)
			Arguments.of(product, LocalDate.of(2026, 2, 14), unit, "2월 15일 전날: 여전히 100만원"),
			Arguments.of(product, LocalDate.of(2026, 2, 15), unit.multiply(BigDecimal.valueOf(2)),
				"2월 15일: 첫 정기 납입 발생 (총 200만원)"),
			Arguments.of(product, LocalDate.of(2026, 2, 16), unit.multiply(BigDecimal.valueOf(2)),
				"2월 16일: 첫 정기 납입 다음날 (총 200만원)"),
			// 3월 정기 납입
			Arguments.of(product, LocalDate.of(2026, 3, 15), unit.multiply(BigDecimal.valueOf(3)),
				"3월 15일: 두번째 정기 납입 발생 (총 300만원)"),
			// 12월 정기 납입
			Arguments.of(product, LocalDate.of(2026, 12, 15), unit.multiply(BigDecimal.valueOf(12)),
				"12월 15일: 열두번째 정기 납입 발생 (총 1200만원)"),
			// 만기일 이후 (예: 만기 12개월 가정시, 초회 1회 + 정기 11 = 총 12회)
			Arguments.of(product, LocalDate.of(2027, 1, 1), unit.multiply(BigDecimal.valueOf(12)),
				"만기일: 최종 12회차 적립 완료 (총 1200만원)")
		);
	}

	public static Stream<Arguments> provideRemainingDaysSource() {
		FinancialProduct cash = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct deposit = FinancialProductDataProvider.createDepositProduct("user-1");
		FinancialProduct savings = FinancialProductDataProvider.createSavingsProduct("user-1");
		return Stream.of(
			Arguments.of(cash, LocalDate.of(2026, 1, 1), 0L, "현금 상품: 현금 상품은 0을 반환해야 한다."),
			Arguments.of(deposit, LocalDate.of(2027, 1, 2), 0L, "예금 상품: 기준일자가 만기일 이후인 경우 남은 일수는 0이 반환해야 한다."),
			Arguments.of(deposit, LocalDate.of(2027, 1, 1), 0L, "예금 상품: 기준일자가 만기일과 동일한 경우 남은 일수는 0이 반환해야 한다."),
			Arguments.of(deposit, LocalDate.of(2026, 2, 27), 308L, "예금 상품: 기준일자가 만기일 이전인 경우 남은 일수는 0보다 큰 값이 반환해야 한다."),
			Arguments.of(savings, LocalDate.of(2027, 1, 2), 0L, "적금 상품: 기준일자가 만기일 이후인 경우 남은 일수는 0이 반환해야 한다."),
			Arguments.of(savings, LocalDate.of(2027, 1, 1), 0L, "적금 상품: 기준일자가 만기일과 동일한 경우 남은 일수는 0이 반환해야 한다."),
			Arguments.of(savings, LocalDate.of(2026, 2, 27), 308L, "적금 상품: 기준일자가 만기일 이전인 경우 남은 일수는 0보다 큰 값이 반환해야 한다.")
		);
	}

	public static Stream<Arguments> provideProgressSource() {
		FinancialProduct cash = FinancialProductDataProvider.createCashProduct("user-1");
		FinancialProduct deposit = FinancialProductDataProvider.createDepositProduct("user-1");
		FinancialProduct savings = FinancialProductDataProvider.createSavingsProduct("user-1");
		return Stream.of(
			Arguments.of(cash, LocalDate.of(2026, 1, 1).minusDays(1), BigDecimal.ONE,
				"현금 상품: 기준일자가 시작일자 이전이어도 1.0을 반환해야 한다"),
			Arguments.of(cash, LocalDate.of(2026, 1, 1), BigDecimal.ONE, "현금 상품: 기준일자가 시작일자와 동일해도 1.0을 반환해야 한다"),
			Arguments.of(cash, LocalDate.of(2026, 1, 2), BigDecimal.ONE, "현금 상품: 기준일자가 시작일자보다 이후여도 1.0을 반환해야 한다"),
			Arguments.of(deposit, LocalDate.of(2026, 1, 1).minusMonths(2), BigDecimal.ZERO,
				"예금 상품: 기준일자가 시작일자보다 이전인 경우 진행률은 0.0이 반환해야 된다."),
			Arguments.of(deposit, LocalDate.of(2027, 1, 2), BigDecimal.ONE, "예금 상품: 기준일자가 만기일 이후인 경우 진행률은 1.0이 반환된다."),
			Arguments.of(deposit, LocalDate.of(2027, 1, 1), BigDecimal.ONE, "예금 상품: 기준일자가 만기일과 동일한 경우 진행률은 1.0이 반환된다."),
			Arguments.of(deposit, LocalDate.of(2026, 2, 27), BigDecimal.valueOf(0.16),
				"예금 상품: 기준일자가 만기일 이전인 경우 진행률은 0.0과 1.0 사이의 값이 반환된다."),
			Arguments.of(savings, LocalDate.of(2026, 1, 1).minusMonths(2), BigDecimal.ZERO,
				"적금 상품: 기준일자가 시작일자보다 이전인 경우 진행률은 0.0이 반환된다."),
			Arguments.of(savings, LocalDate.of(2027, 1, 2), BigDecimal.ONE,
				"적금 상품: 기준일자가 만기일 이후인 경우 진행률은 1.0이 반환된다."),
			Arguments.of(savings, LocalDate.of(2027, 1, 1), BigDecimal.ONE,
				"적금 상품: 기준일자가 만기일과 동일한 경우 진행률은 1.0이 반환된다."),
			Arguments.of(savings, LocalDate.of(2026, 2, 27), BigDecimal.valueOf(0.16),
				"적금 상품: 기준일자가 만기일 이전인 경우 진행률은 0.0과 1.0 사이의 값이 반환된다.")
		);
	}
}
