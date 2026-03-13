package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.money.domain.Money;

public class MonthlyInstallmentInvestmentAmount implements InstallmentInvestmentAmount {

	private final Money amount;

	public MonthlyInstallmentInvestmentAmount(Money amount) {
		this.amount = amount;
		if (this.amount.isNegative()) {
			throw new IllegalArgumentException("금액은 음수일 수 없습니다.");
		}
	}

	@Override
	public Money getMonthlyAmount() {
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
		return amount.getValue();
	}
}
