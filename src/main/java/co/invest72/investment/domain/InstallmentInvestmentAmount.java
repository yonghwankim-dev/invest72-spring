package co.invest72.investment.domain;

import java.math.BigDecimal;

public interface InstallmentInvestmentAmount extends InvestmentAmount {
	BigDecimal getMonthlyAmount();
}
