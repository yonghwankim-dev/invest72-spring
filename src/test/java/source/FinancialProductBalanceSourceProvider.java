package source;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import co.invest72.financial_product.domain.FinancialProduct;

public class FinancialProductBalanceSourceProvider {

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
		return Stream.of(
			Arguments.of(product, LocalDate.of(2026, 1, 1).minusMonths(2), BigDecimal.ZERO,
				"적금 상품: 기준일자가 시작일자보다 이전인 경우 잔액은 0이 반환해야 한다."),
			Arguments.of(product, LocalDate.of(2027, 1, 2),
				BigDecimal.valueOf(1_000_000L).multiply(BigDecimal.valueOf(12)),
				"적금 상품: 기준일자가 만기일 이후인 경우 잔액은 월 적립액 * 총 개월 수가 반환해야 한다."),
			Arguments.of(product, LocalDate.of(2026, 2, 14), BigDecimal.valueOf(1_000_000),
				"적금 상품: 기준일자의 days가 납입일 이전이라면 잔액이 1개월분 적립되어야 한다."),
			Arguments.of(product, LocalDate.of(2026, 2, 15), BigDecimal.valueOf(2_000_000),
				"적금 상품: 기준일자의 days가 납입일과 동일하면 잔액이 2개월분 적립되어야 한다."),
			Arguments.of(product, LocalDate.of(2026, 2, 16), BigDecimal.valueOf(2_000_000),
				"적금 상품: 기준일자의 days가 납입일 이후라면 잔액이 2개월분 적립되어야 한다")
		);
	}
}
