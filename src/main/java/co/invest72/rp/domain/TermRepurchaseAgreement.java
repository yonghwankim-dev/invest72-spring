package co.invest72.rp.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.money.domain.Money;
import lombok.Builder;

public class TermRepurchaseAgreement implements RepurchaseAgreement {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final LocalDate startDate;
	private final InvestPeriod investPeriod;

	@Builder(toBuilder = true)
	public TermRepurchaseAgreement(InvestmentAmount investmentAmount, InterestRate interestRate, LocalDate startDate,
		InvestPeriod investPeriod) {
		this.investmentAmount = Objects.requireNonNull(investmentAmount);
		this.interestRate = Objects.requireNonNull(interestRate);
		this.startDate = Objects.requireNonNull(startDate);
		this.investPeriod = Objects.requireNonNull(investPeriod);
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
		Money interest = investmentAmount.calAnnualInterest(interestRate)
			.times(days)
			.divide(BigDecimal.valueOf(365L));
		if (interest.isNegative()) {
			return Money.of(BigDecimal.ZERO, interest.getCurrency());
		}
		return Investment.roundToWholeMoney.apply(interest);
	}

	@Override
	public LocalDate getExpirationDate() {
		return startDate.plusDays(investPeriod.getDays(this.startDate));
	}

	/**
	 * RP 상품의 만기 이자 금액 계산
	 * <p>
	 * 만기 이자 금액 = (원금 x 연이자율) x 약정 일수 / 365
	 * @return Money
	 */
	@Override
	public Money calculateMaturityInterest() {
		return investmentAmount.calAnnualInterest(interestRate)
			.times(investPeriod.getDays(startDate))
			.divide(BigDecimal.valueOf(365))
			.roundToWhole();
	}
}
