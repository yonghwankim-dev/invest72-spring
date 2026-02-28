package co.invest72.investment.domain.investment;

import java.time.LocalDate;

public interface ExpirationCalculator {

	LocalDate calculate(LocalDate startDate, int months);
}
