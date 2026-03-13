package co.invest72.investment.domain.amount;

import java.math.BigDecimal;
import java.math.RoundingMode;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.money.domain.Money;

public class YearlyInstallmentInvestmentAmount implements InstallmentInvestmentAmount {
	private final Money amount;

	public YearlyInstallmentInvestmentAmount(Money amount) {
		this.amount = amount;
		if (this.amount.isNegative()) {
			throw new IllegalArgumentException("금액은 음수일 수 없습니다.");
		}
	}

	@Override
	public Money getMonthlyAmount() {
		BigDecimal monthlyAmount = amount.getValue()
			.divide(BigDecimal.valueOf(12), RoundingMode.HALF_EVEN);
		return Money.of(monthlyAmount, amount.getCurrency().getCode());
	}

	@Override
	public BigDecimal calAnnualInterest(InterestRate interestRate) {
		return interestRate.getAnnualInterest(amount.getValue());
	}

	@Override
	public BigDecimal calMonthlyInterest(InterestRate interestRate) {
		return interestRate.calMonthlyInterest(amount.getValue());
	}

	@Override
	public BigDecimal getAmount() {
		return amount.getValue();
	}
}
