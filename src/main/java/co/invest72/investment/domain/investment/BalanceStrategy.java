package co.invest72.investment.domain.investment;

import java.time.LocalDate;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.money.domain.Money;

public interface BalanceStrategy {
	Money calculate(FinancialProduct product, LocalDate today, LocalDate expirationDate);
}
