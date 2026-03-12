package co.invest72.investment.domain.amount;

import java.math.BigDecimal;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.money.domain.Money;

public class MonthlyInstallmentInvestmentAmount implements InstallmentInvestmentAmount {

	private final BigDecimal value;
	private final Money moneyValue;

	public MonthlyInstallmentInvestmentAmount(int value) {
		this(BigDecimal.valueOf(value));
	}

	public MonthlyInstallmentInvestmentAmount(BigDecimal value) {
		this(Money.of(value, "KRW"));
	}

	public MonthlyInstallmentInvestmentAmount(Money money) {
		this.value = money.getValue();
		this.moneyValue = money;
		if (this.moneyValue.getValue().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("금액은 음수일 수 없습니다.");
		}
	}

	@Override
	public BigDecimal getMonthlyAmount() {
		return this.value;
	}

	@Override
	public BigDecimal calAnnualInterest(InterestRate interestRate) {
		return interestRate.getAnnualInterest(value);
	}

	@Override
	public BigDecimal calMonthlyInterest(InterestRate interestRate) {
		return interestRate.calMonthlyInterest(value);
	}

	@Override
	public BigDecimal getAmount() {
		return value;
	}
}
