package co.invest72.investment.domain.amount;

import java.math.BigDecimal;
import java.math.RoundingMode;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;

public class YearlyInstallmentInvestmentAmount implements InstallmentInvestmentAmount {
	private final BigDecimal amount;

	public YearlyInstallmentInvestmentAmount(int amount) {
		this(BigDecimal.valueOf(amount));
	}

	public YearlyInstallmentInvestmentAmount(BigDecimal amount) {
		this.amount = amount;
		if (this.amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("investment.Investment amount must be non-negative.");
		}
	}

	@Override
	public BigDecimal getMonthlyAmount() {
		return this.amount.divide(BigDecimal.valueOf(12), RoundingMode.HALF_EVEN);
	}

	@Override
	public BigDecimal calAnnualInterest(InterestRate interestRate) {
		return interestRate.getAnnualInterest(amount);
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
