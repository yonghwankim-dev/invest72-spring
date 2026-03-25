package source;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import co.invest72.financial_product.domain.FinancialProduct;

public class SavingsProductBalanceSourceProvider {

	public static Stream<Arguments> provideBalanceSource() {
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct("user-1");
		return Stream.of(
			Arguments.of(product, LocalDate.of(2026, 1, 1).minusMonths(2), BigDecimal.ZERO,
				"기준일자가 시작일자보다 이전인 경우 잔액은 0이 반환해야 한다."),
			Arguments.of(product, LocalDate.of(2027, 1, 2),
				BigDecimal.valueOf(1_000_000L).multiply(BigDecimal.valueOf(12)),
				"기준일자가 만기일 이후인 경우 잔액은 월 적립액 * 총 개월 수가 반환해야 한다."),
			Arguments.of(product, LocalDate.of(2026, 2, 14), BigDecimal.valueOf(1_000_000),
				"기준일자의 days가 납입일 이전이라면 잔액이 1개월분 적립되어야 한다."),
			Arguments.of(product, LocalDate.of(2026, 2, 15), BigDecimal.valueOf(2_000_000),
				"기준일자의 days가 납입일과 동일하면 잔액이 2개월분 적립되어야 한다."),
			Arguments.of(product, LocalDate.of(2026, 2, 16), BigDecimal.valueOf(2_000_000),
				"기준일자의 days가 납입일 이후라면 잔액이 2개월분 적립되어야 한다")
		);
	}
}
