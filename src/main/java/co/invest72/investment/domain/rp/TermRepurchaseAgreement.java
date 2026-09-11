package co.invest72.investment.domain.rp;

import java.time.LocalDate;

public class TermRepurchaseAgreement implements RepurchaseAgreement {

	@Override
	public LocalDate calculateExpirationDate(LocalDate startDate, int daysToAdd) {
		return startDate.plusDays(daysToAdd);
	}
}
