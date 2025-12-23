package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;

public class CompoundFixedInstallmentSavingMonthlyDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public CompoundFixedInstallmentSavingMonthlyDetailFactory(InvestmentAmount investmentAmount,
		InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<MonthlyInvestmentDetail> createDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;

		result.add(new MonthlyInvestmentDetail(0, principal, interest, profit));
		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			principal = profit.add(investmentAmount.getAmount());
			interest = interestRate.getMonthlyRate().multiply(principal);
			profit = principal.add(interest);
			result.add(new MonthlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}
}
