package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;

public class YearlyInstallmentInvestmentAmount implements InstallmentInvestmentAmount {
	private final int amount;

	public YearlyInstallmentInvestmentAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public int getMonthlyAmount() {
		return amount / 12;
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
