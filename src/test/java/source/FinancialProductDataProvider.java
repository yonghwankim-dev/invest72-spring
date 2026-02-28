package source;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductRate;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;

public class FinancialProductDataProvider {
	public static FinancialProduct createCashProduct(String userId) {
		return FinancialProduct.builder()
			.userId(userId)
			.name("현금 상품")
			.investmentType(InvestmentType.CASH)
			.amount(new ProductAmount(BigDecimal.valueOf(1_000_000L)))
			.months(new ProductMonths(0))
			.interestRate(new ProductRate(BigDecimal.valueOf(0.0)))
			.interestType(InterestType.NONE)
			.taxType(TaxType.NONE)
			.taxRate(new ProductRate(BigDecimal.valueOf(0.0)))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
	}

	public static FinancialProduct createDepositProduct(String userId) {
		return FinancialProduct.builder()
			.userId(userId)
			.name("예금 상품")
			.investmentType(InvestmentType.DEPOSIT)
			.amount(new ProductAmount(BigDecimal.valueOf(1_000_000L)))
			.months(new ProductMonths(12))
			.interestRate(new ProductRate(BigDecimal.valueOf(0.05)))
			.interestType(InterestType.SIMPLE)
			.taxType(TaxType.STANDARD)
			.taxRate(new ProductRate(BigDecimal.valueOf(0.154)))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
	}
}
