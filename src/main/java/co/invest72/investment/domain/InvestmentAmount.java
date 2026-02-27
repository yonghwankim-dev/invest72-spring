package co.invest72.investment.domain;

import java.math.BigDecimal;

public interface InvestmentAmount {
	double calAnnualInterest(InterestRate interestRate);

	BigDecimal calMonthlyInterest(InterestRate interestRate);

	BigDecimal getAmount();
}
