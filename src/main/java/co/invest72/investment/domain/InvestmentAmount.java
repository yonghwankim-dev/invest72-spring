package co.invest72.investment.domain;

import co.invest72.money.domain.Money;

public interface InvestmentAmount {

	Money calAnnualInterest(InterestRate interestRate);

	Money calMonthlyInterest(InterestRate interestRate);

	Money getAmountMoney();
}
