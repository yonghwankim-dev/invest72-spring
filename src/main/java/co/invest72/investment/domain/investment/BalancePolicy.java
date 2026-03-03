package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;

public enum BalancePolicy implements BalanceStrategy {

	// 투자 금액이 고정된 경우(예: 현금, 예금)
	FIXED {
		@Override
		public BigDecimal calculate(FinancialProduct product,
			LocalDate today) {
			return product.getAmount().getValue();
		}
	},
	// 투자 금액이 시간이 지남에 따라 누적되는 경우(예: 적금)
	ACCUMULATIVE {
		@Override
		public BigDecimal calculate(FinancialProduct product, LocalDate today) {
			LocalDate startDate = product.getStartDate();
			LocalDate expirationDate = product.getExpirationDate();
			ProductAmount amount = product.getAmount();
			ProductMonths months = product.getMonths();

			if (today.isBefore(startDate)) {
				return BigDecimal.ZERO;
			}
			if (today.isAfter(expirationDate)) {
				return amount.getValue().multiply(BigDecimal.valueOf(months.getValue()));
			}
			long elapsedMonths = ChronoUnit.MONTHS.between(startDate, today);
			if (product.isPaidOn(today)) {
				elapsedMonths++;
			}
			return amount.getValue().multiply(BigDecimal.valueOf(elapsedMonths));
		}
	}
}
