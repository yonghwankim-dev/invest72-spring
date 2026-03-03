package source;

import static co.invest72.investment.domain.interest.InterestType.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.investment.domain.CashInvestment;
import co.invest72.investment.domain.investment.CompoundFixedDeposit;
import co.invest72.investment.domain.investment.CompoundFixedInstallmentSaving;
import co.invest72.investment.domain.investment.SimpleFixedDeposit;
import co.invest72.investment.domain.investment.SimpleFixedInstallmentSaving;

public class FinancialProductTestDataProvider {
	public static Stream<Arguments> provideFinancialProducts() {

		FinancialProduct product1 = FinancialProductDataProvider.createDepositProduct("test-user-1", SIMPLE);
		FinancialProduct product2 = FinancialProductDataProvider.createDepositProduct("test-user-1", COMPOUND);
		FinancialProduct product3 = FinancialProductDataProvider.createSavingsProduct("test-user-1", SIMPLE);
		FinancialProduct product4 = FinancialProductDataProvider.createSavingsProduct("test-user-1", COMPOUND);
		FinancialProduct product5 = FinancialProductDataProvider.createCashProduct("test-user-1");

		return Stream.of(
			Arguments.of(product1, SimpleFixedDeposit.class, product1.getName()),
			Arguments.of(product2, CompoundFixedDeposit.class, product2.getName()),
			Arguments.of(product3, SimpleFixedInstallmentSaving.class, product3.getName()),
			Arguments.of(product4, CompoundFixedInstallmentSaving.class, product4.getName()),
			Arguments.of(product5, CashInvestment.class, product5.getName())
		);
	}

	public static Stream<Arguments> daysBeforePaymentDay() {
		return Stream.of(
			Arguments.of(LocalDate.of(2026, 2, 14), BigDecimal.valueOf(1_000_000)),
			Arguments.of(LocalDate.of(2026, 2, 16), BigDecimal.valueOf(2_000_000))
		);
	}

}
