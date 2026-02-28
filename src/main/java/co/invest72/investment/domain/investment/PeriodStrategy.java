package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PeriodStrategy {
	LocalDate calculateExpiration(LocalDate startDate, int months);

	BigDecimal calculateProgress(LocalDate startDate, LocalDate expirationDate, LocalDate today);

	long remainingDays(LocalDate today, LocalDate expirationDate);
}
