package co.invest72.investment.domain.investment;

import co.invest72.investment.domain.InterestRate;
import co.invest72.money.domain.Money;

public class SimpleInterestCalculator implements InterestCalculator {
	@Override
	public Money calculate(Money originalPrincipal, Money currentPrincipal, InterestRate interestRate) {
		return interestRate.calMonthlyInterest(originalPrincipal); // 항상 초기 원금 기준
	}
}
