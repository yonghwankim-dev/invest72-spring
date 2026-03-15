package co.invest72.investment.domain;

import co.invest72.money.domain.Money;

public interface InstallmentInvestmentAmount extends InvestmentAmount {
	Money getMonthlyAmount();
}
