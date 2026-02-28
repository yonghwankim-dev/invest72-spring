package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

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
			ProductAmount amount = product.getAmount();
			ProductMonths months = product.getMonths();

			if (today.isBefore(startDate)) {
				return BigDecimal.ZERO;
			}
			if (today.isAfter(product.getExpirationDate())) {
				return amount.getValue().multiply(BigDecimal.valueOf(months.getValue()));
			}
			long elapsedMonths = startDate.until(today, java.time.temporal.ChronoUnit.MONTHS);
			return amount.getValue().multiply(BigDecimal.valueOf(elapsedMonths));
		}
	}
}
