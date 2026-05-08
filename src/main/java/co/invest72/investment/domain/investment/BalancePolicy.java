package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import co.invest72.financial_product.domain.FinancialProduct;
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

			if (today.isBefore(startDate)) {
				return Money.of(BigDecimal.ZERO, productCurrency);
			}

			// 초회납입
			long count = 1;
			// 정기납입 계산
			// 예: 가입 1월 -> 계산 시작점 2월 1일
			LocalDate regularPaymentStandardDate = startDate.plusMonths(1).withDayOfMonth(1);

			if (!today.isBefore(regularPaymentStandardDate)) {
				long monthsSinceNextMonth = ChronoUnit.MONTHS.between(regularPaymentStandardDate,
					today.withDayOfMonth(1));
				count += monthsSinceNextMonth;

				// 오늘이 이번 달 납입 이후라면 1회 추가
				if (product.isPaidOn(today)) {
					count++;
				}
			}

			// 약정된 총 횟수(예: 12회)를 넘지 않도록 제한
			long finalCount = Math.min(count, product.getMonths().getValue());

			Money money = Money.of(product.getAmount().getValue(), productCurrency);
			return money.times(BigDecimal.valueOf(finalCount));
		}
	}
}
