package co.invest72.financial_product.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.investment.domain.investment.InvestmentType;

public class FinancialProductCalculator {

	public LocalDate calculateExpirationDate(FinancialProduct product) {
		InvestmentType investmentType = InvestmentType.valueOf(product.getInvestmentTypeName());
		LocalDate startDate = product.getStartDate();
		Integer months = product.getMonthsValue();

		return investmentType.calculateExpirationDate(startDate, months);
	}

	public BigDecimal calculateBalance(FinancialProduct product, LocalDate today) {
		InvestmentType investmentType = InvestmentType.valueOf(product.getInvestmentTypeName());
		LocalDate expirationDate = calculateExpirationDate(product);

		return investmentType.calculateBalance(product, today, expirationDate);
	}

	public BigDecimal calculateProgress(FinancialProduct product, LocalDate today) {
		InvestmentType investmentType = InvestmentType.valueOf(product.getInvestmentTypeName());
		LocalDate startDate = product.getStartDate();
		LocalDate expirationDate = calculateExpirationDate(product);

		return investmentType.calculateProgress(startDate, expirationDate, today);
	}

	public Long calculateRemainingDays(FinancialProduct product, LocalDate today) {
		InvestmentType investmentType = InvestmentType.valueOf(product.getInvestmentTypeName());
		LocalDate expirationDate = calculateExpirationDate(product);

		return investmentType.calculateRemainingDays(today, expirationDate);
	}
}
