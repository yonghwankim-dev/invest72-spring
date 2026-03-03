package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;

public class MonthlyInstallmentInvestmentAmount implements InstallmentInvestmentAmount {

	private final BigDecimal value;

	public MonthlyInstallmentInvestmentAmount(int value) {
		this(BigDecimal.valueOf(value));
	}

	public MonthlyInstallmentInvestmentAmount(BigDecimal value) {
		this.value = value;
		if (value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("investment.Investment amount must be non-negative.");
		}
	}

	@Override
	public BigDecimal getMonthlyAmount() {
		return this.value;
	}

	@Override
	public BigDecimal calAnnualInterest(InterestRate interestRate) {
		return interestRate.getAnnualInterest(value);
	}

	@Override
	public BigDecimal calMonthlyInterest(InterestRate interestRate) {
		return interestRate.calMonthlyInterest(value);
	}

	@Override
	public BigDecimal getAmount() {
		return value;
	}
}
