package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.financial_product.domain.FinancialProduct;

public interface BalanceStrategy {
	BigDecimal calculate(FinancialProduct product, LocalDate today);
}
