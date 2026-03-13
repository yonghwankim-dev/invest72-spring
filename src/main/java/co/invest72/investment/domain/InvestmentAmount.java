package co.invest72.investment.domain;

import java.math.BigDecimal;

import co.invest72.money.domain.Money;

public interface InvestmentAmount {

	Money calAnnualInterest(InterestRate interestRate);

	BigDecimal calMonthlyInterest(InterestRate interestRate);

	BigDecimal getAmount();
}
