package co.invest72.investment.domain.amount;

import java.math.BigDecimal;
import java.math.RoundingMode;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.money.domain.Money;

public class YearlyInstallmentInvestmentAmount implements InstallmentInvestmentAmount {
	private final BigDecimal amount;
	private final Money moneyAmount;

	public YearlyInstallmentInvestmentAmount(Money moneyAmount) {
		this.amount = moneyAmount.getValue();
		this.moneyAmount = moneyAmount;
		if (this.moneyAmount.getValue().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("금액은 음수일 수 없습니다.");
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
