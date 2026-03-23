package co.invest72.financial_product.domain.service;

import java.time.LocalDate;

import co.invest72.financial_product.domain.FinancialProduct;

public class ExpirationDateCalculator {

	public LocalDate calculate(FinancialProduct product) {
		return LocalDate.now();
	}
}
