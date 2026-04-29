package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;

public enum BalancePolicy implements BalanceStrategy {

	// 투자 금액이 고정된 경우(예: 현금, 예금)
	FIXED {
		@Override
		public Money calculate(FinancialProduct product, Currency productCurrency, LocalDate today,
			LocalDate expirationDate) {
			return Money.of(product.getAmount().getValue(), productCurrency);
		}
	},
	// 투자 금액이 시간이 지남에 따라 누적되는 경우(예: 적금)
	ACCUMULATIVE {
		@Override
		public Money calculate(FinancialProduct product, Currency productCurrency, LocalDate today,
			LocalDate expirationDate) {
			LocalDate startDate = product.getStartDate();
			ProductAmount amount = product.getAmount();
			ProductMonths months = product.getMonths();

			if (today.isBefore(startDate)) {
				return Money.of(BigDecimal.ZERO, productCurrency);
			}
			if (today.isAfter(expirationDate)) {
				Money money = Money.of(amount.getValue(), productCurrency);
				return money.times(BigDecimal.valueOf(months.getValue()));
			}
			long elapsedMonths = ChronoUnit.MONTHS.between(startDate, today);
			if (product.isPaidOn(today)) {
				elapsedMonths++;
			}
			Money money = Money.of(amount.getValue(), productCurrency);
			return money.times(BigDecimal.valueOf(elapsedMonths));
		}
	}
}
