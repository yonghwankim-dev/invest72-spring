package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.LumpSumInvestmentAmount;

public class FixedDepositAmount implements LumpSumInvestmentAmount {

	private final BigDecimal amount;

	public FixedDepositAmount(int amount) {
		this(BigDecimal.valueOf(amount));
	}

	public FixedDepositAmount(BigDecimal amount) {
		this.amount = amount;
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("investment.Investment amount must be non-negative.");
		}
	}

	@Override
	public BigDecimal getDepositAmount() {
		return amount;
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
		return getDepositAmount();
	}
}
