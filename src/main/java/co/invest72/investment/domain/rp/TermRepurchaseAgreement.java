package co.invest72.investment.domain.rp;

import java.time.LocalDate;

public class TermRepurchaseAgreement implements RepurchaseAgreement {

	@Override
	public LocalDate calculateExpirationDate(LocalDate startDate, int daysToAdd) {
		if (daysToAdd <= 0) {
			return startDate;
		}
		return startDate.plusDays(daysToAdd);
	}
}
