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
			if (months < 0) {
				throw new IllegalArgumentException("개월 수는 음수일 수 없습니다.");
			}
			return startDate.plusMonths(months);
		}

		@Override
		public BigDecimal calculateProgress(LocalDate startDate, LocalDate expirationDate, LocalDate today) {
			if (startDate.isEqual(expirationDate)) {
				return BigDecimal.ONE; // 시작일자와 만기일자가 동일한 경우 진행률은 100%
			}
			if (startDate.isAfter(expirationDate)) {
				throw new IllegalArgumentException("시작일자는 만기일자보다 이전이어야 합니다.");
			}
			if (today.isBefore(startDate)) {
				return BigDecimal.ZERO;
			}
			if (today.isAfter(expirationDate)) {
				return BigDecimal.ONE;
			}
			try {
				long totalDays = startDate.until(expirationDate, ChronoUnit.DAYS);
				long elapsedDays = startDate.until(today, ChronoUnit.DAYS);
				return BigDecimal.valueOf(elapsedDays)
					.divide(BigDecimal.valueOf(totalDays), 2, RoundingMode.HALF_EVEN);
			} catch (ArithmeticException e) {
				return BigDecimal.ZERO; // 총 일수가 0인 경우 진행률을 0으로 처리
			}

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
