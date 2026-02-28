package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ProgressCalculator {
	BigDecimal calculate(LocalDate startDate, LocalDate expirationDate, LocalDate today);
}
