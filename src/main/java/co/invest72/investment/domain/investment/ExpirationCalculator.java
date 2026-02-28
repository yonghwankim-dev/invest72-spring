package co.invest72.investment.domain.investment;

import java.time.LocalDate;

public interface ExpirationCalculator {

	LocalDate calculateExpirationDate(LocalDate startDate, int months);
}
