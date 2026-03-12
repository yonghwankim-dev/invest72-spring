package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.money.domain.Money;

public class FixedDepositAmount implements LumpSumInvestmentAmount {

	private final Money moneyAmount;

	public FixedDepositAmount(BigDecimal amount, String currency) {
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("investment.Investment amount must be non-negative.");
		}
		this.moneyAmount = Money.of(amount, currency);
	}

	@Override
	public BigDecimal getDepositAmount() {
		return moneyAmount.getValue();
	}

	@Override
	public Money getDepositAmount_temp() {
		return moneyAmount;
	}

	@Override
	public BigDecimal calAnnualInterest(InterestRate interestRate) {
		return interestRate.getAnnualInterest(moneyAmount.getValue());
	}

	@Override
	public BigDecimal calMonthlyInterest(InterestRate interestRate) {
		return interestRate.calMonthlyInterest(moneyAmount.getValue());
	}

	@Override
	public BigDecimal getAmount() {
		return getDepositAmount();
	}
}
