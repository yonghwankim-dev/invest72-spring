package co.invest72.financial_product.domain.service;

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
}
