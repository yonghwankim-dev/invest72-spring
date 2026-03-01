package source;

import static co.invest72.investment.domain.interest.InterestType.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductRate;
import co.invest72.investment.domain.CashInvestment;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.CompoundFixedDeposit;
import co.invest72.investment.domain.investment.CompoundFixedInstallmentSaving;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.SimpleFixedDeposit;
import co.invest72.investment.domain.investment.SimpleFixedInstallmentSaving;
import co.invest72.investment.domain.tax.TaxType;

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

	private static FinancialProduct createFinancialProduct(String name, InvestmentType investmentType,
		InterestType interestType) {
		return FinancialProduct.builder()
			.userId("user-" + UUID.randomUUID())
			.name(name)
			.investmentType(investmentType)
			.amount(new ProductAmount(BigDecimal.valueOf(1_000_000)))
			.months(new ProductMonths(12))
			.interestRate(new ProductRate(BigDecimal.valueOf(0.05)))
			.interestType(interestType)
			.taxType(TaxType.NON_TAX)
			.taxRate(new ProductRate(BigDecimal.ZERO))
			.startDate(LocalDate.now())
			.createdAt(LocalDateTime.now())
			.build();
	}
}
