package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum PeriodPolicy implements PeriodStrategy {
	// 일반적인 예/적금 정책
	STANDARD {
		@Override
		public LocalDate calculateExpiration(LocalDate startDate, int months) {
			return startDate.plusMonths(months);
		}

		@Override
		public BigDecimal calculateProgress(LocalDate startDate, LocalDate expirationDate, LocalDate today) {
			if (today.isBefore(startDate)) {
				return BigDecimal.ZERO;
			}
			if (today.isAfter(expirationDate)) {
				return BigDecimal.ONE;
			}
			long totalDays = startDate.until(expirationDate, ChronoUnit.DAYS);
			long elapsedDays = startDate.until(today, ChronoUnit.DAYS);
			return BigDecimal.valueOf(elapsedDays)
				.divide(BigDecimal.valueOf(totalDays), 4, RoundingMode.HALF_EVEN);
		}

		@Override
		public long remainingDays(LocalDate today, LocalDate expirationDate) {
			if (today.isAfter(expirationDate)) {
				return 0;
			}
			return today.until(expirationDate, ChronoUnit.DAYS);
		}
	},

	// 만기가 없는 자산(현금, 주식 등) 정책
	INDEFINITE {
		@Override
		public LocalDate calculateExpiration(LocalDate startDate, int months) {
			return LocalDate.MAX;
		}

		@Override
		public BigDecimal calculateProgress(LocalDate startDate, LocalDate expirationDate, LocalDate today) {
			return BigDecimal.ONE; // 만기가 없는 자산은 항상 100% 진행된 것으로 간주
		}

		@Override
		public long remainingDays(LocalDate today, LocalDate expirationDate) {
			return 0; // 만기가 없는 자산은 남은 일수가 0
		}
	}
}
