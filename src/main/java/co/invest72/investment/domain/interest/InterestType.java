package co.invest72.investment.domain.interest;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.investment.CompoundInterestCalculator;
import co.invest72.investment.domain.investment.InterestCalculator;
import co.invest72.investment.domain.investment.NoneInterestCalculator;
import co.invest72.investment.domain.investment.SimpleInterestCalculator;
import co.invest72.money.domain.Money;
import lombok.Getter;

public enum InterestType {
	NONE("없음", new NoneInterestCalculator()),
	SIMPLE("단리", new SimpleInterestCalculator()),
	COMPOUND("복리", new CompoundInterestCalculator());

	@Getter
	private final String typeName;
	private final InterestCalculator interestCalculator;

	InterestType(String typeName, InterestCalculator interestCalculator) {
		this.typeName = typeName;
		this.interestCalculator = interestCalculator;
	}

	public Money calculateInterest(Money originalPrincipal, Money currentPrincipal, InterestRate interestRate) {
		return this.interestCalculator.calculate(originalPrincipal, currentPrincipal, interestRate);
	}
}
