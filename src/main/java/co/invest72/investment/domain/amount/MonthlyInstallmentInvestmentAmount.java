package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;

public class MonthlyInstallmentInvestmentAmount implements InstallmentInvestmentAmount {

	private final int amount;

	public MonthlyInstallmentInvestmentAmount(int amount) {
		this.amount = amount;
		if (amount < 0) {
			throw new IllegalArgumentException("investment.Investment amount must be non-negative.");
		}
	}

	@Override
	public int getMonthlyAmount() {
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
		return BigDecimal.valueOf(amount);
	}
}
