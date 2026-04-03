package co.invest72.investment.domain.investment;

import co.invest72.investment.domain.InterestRate;
import co.invest72.money.domain.Money;

public class CompoundInterestCalculator implements InterestCalculator {
	@Override
	public Money calculate(Money originalPrincipal, Money currentPrincipal, InterestRate interestRate) {
		return interestRate.calMonthlyInterest(currentPrincipal); // 매월 변하는 원리금 기준
	}
}
