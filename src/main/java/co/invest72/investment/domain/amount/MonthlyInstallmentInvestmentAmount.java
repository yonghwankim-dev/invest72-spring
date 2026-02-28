package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;

public class MonthlyInstallmentInvestmentAmount implements InstallmentInvestmentAmount {

	private final BigDecimal amount;

	public MonthlyInstallmentInvestmentAmount(int amount) {
		this(BigDecimal.valueOf(amount));
	}

	public MonthlyInstallmentInvestmentAmount(BigDecimal amount) {
		this.amount = amount;
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("investment.Investment amount must be non-negative.");
		}
	}

	@Override
	public BigDecimal getMonthlyAmount() {
		return this.amount;
	}

	@Override
	public double calAnnualInterest(InterestRate interestRate) {
		return interestRate.getAnnualInterest(amount).doubleValue();
	}

	@Override
	public BigDecimal calMonthlyInterest(InterestRate interestRate) {
		return interestRate.calMonthlyInterest(amount);
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}
}
