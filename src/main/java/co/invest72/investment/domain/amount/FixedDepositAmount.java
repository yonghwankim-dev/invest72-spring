package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.money.domain.Money;

public class FixedDepositAmount implements LumpSumInvestmentAmount {

	private final Money amount;

	public FixedDepositAmount(BigDecimal amount, String currency) {
		this(Money.of(amount, currency));
	}

	public FixedDepositAmount(Money amount) {
		if (amount.isNegative()) {
			throw new IllegalArgumentException("투자 금액은 음수일 수 없습니다.");
		}
		this.amount = amount;
	}

	@Override
	public Money getDepositAmount() {
		return amount;
	}

	@Override
	public Money calAnnualInterest(InterestRate interestRate) {
		return interestRate.getAnnualInterest(amount);
	}

	@Override
	public BigDecimal calMonthlyInterest(InterestRate interestRate) {
		return interestRate.calMonthlyInterest(amount).getValue();
	}

	@Override
	public BigDecimal getAmount() {
		return getDepositAmount().getValue();
	}
}
