package co.invest72.investment.domain.rp;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.interest.AnnualInterestRate;
import co.invest72.money.domain.Money;
import lombok.Builder;

public class TermRepurchaseAgreement implements RepurchaseAgreement {

	private final InvestmentAmount investmentAmount;
	private final LocalDate startDate;

	@Builder(toBuilder = true)
	public TermRepurchaseAgreement(InvestmentAmount investmentAmount, LocalDate startDate) {
		this.investmentAmount = investmentAmount;
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
	public Money calculateInterestForDays(int days) {
		// 투자 금액 x 약정수익률(연이율) x (예치 일수 / 365)
		InterestRate interestRate = new AnnualInterestRate(BigDecimal.valueOf(0.05));

		Money interest = investmentAmount.calAnnualInterest(interestRate)
			.times(days)
			.divide(BigDecimal.valueOf(365L));

		return Investment.roundToWholeMoney.apply(interest);
	}
}
