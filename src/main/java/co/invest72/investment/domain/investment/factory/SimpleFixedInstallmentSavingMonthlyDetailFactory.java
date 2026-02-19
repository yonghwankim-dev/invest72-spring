package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.MonthlyInvestmentDetail;

public class SimpleFixedInstallmentSavingMonthlyDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public SimpleFixedInstallmentSavingMonthlyDetailFactory(InvestmentAmount investmentAmount,
		InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<MonthlyInvestmentDetail> createDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal accInvestmentAmount = BigDecimal.ZERO;
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;

		result.add(new MonthlyInvestmentDetail(0, principal, interest, profit));
		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			accInvestmentAmount = accInvestmentAmount.add(investmentAmount.getAmount());
			principal = profit.add(investmentAmount.getAmount());
			interest = interestRate.getMonthlyRate().multiply(accInvestmentAmount);
			profit = principal.add(interest);
			result.add(new MonthlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}
}
