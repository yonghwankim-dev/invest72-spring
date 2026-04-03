package co.invest72.investment.domain.investment;

import java.math.BigDecimal;

import co.invest72.investment.domain.InterestRate;
import co.invest72.money.domain.Money;

public class NoneInterestCalculator implements InterestCalculator {
	@Override
	public Money calculate(Money originalPrincipal, Money currentPrincipal, InterestRate interestRate) {
		return originalPrincipal.times(BigDecimal.ZERO);
	}
}
