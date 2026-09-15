package co.invest72.investment.domain.rp;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.money.domain.Money;

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

	@Override
	public Money calculateInterestUntil(int days) {
		// 투자 금액 x 약정수익률(연이율) x (예치 일수 / 365)
		Money investmentAmount = Money.won(1_000_000);
		InterestRate interestRate = new AnnualInterestRate(BigDecimal.valueOf(0.05));

		Money interest = investmentAmount.times(interestRate.getAnnualRate())
			.times(days)
			.divide(BigDecimal.valueOf(365L));

		return Investment.roundToWholeMoney.apply(interest);
	}
}
