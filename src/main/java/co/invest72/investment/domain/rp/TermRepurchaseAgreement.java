package co.invest72.investment.domain.rp;

import java.time.LocalDate;

public class TermRepurchaseAgreement implements RepurchaseAgreement {

	private final LocalDate startDate;

	public TermRepurchaseAgreement(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Override
	public LocalDate calculateExpirationDate(int daysToAdd) {
		if (daysToAdd <= 0) {
			return startDate;
		}
		return startDate.plusDays(daysToAdd);
	}
}
