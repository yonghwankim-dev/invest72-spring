package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.YearlyInvestmentDetail;

public class CompoundFixedInstallmentSavingYearlyDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public CompoundFixedInstallmentSavingYearlyDetailFactory(InvestmentAmount investmentAmount,
		InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<YearlyInvestmentDetail> createDetails() {
		List<YearlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;

		result.add(new YearlyInvestmentDetail(0, principal, interest, profit));
		int years = getFinalYear();
		for (int i = 1; i <= years; i++) {
			BigDecimal months = BigDecimal.valueOf(Math.min(12, investPeriod.getMonths() - (i - 1) * 12));
			BigDecimal value = investmentAmount.getAmount().getValue().multiply(months);
			principal = profit.add(value);

			interest = calculateYearlyInterest(profit, months.intValue());

			profit = principal.add(interest);
			result.add(new YearlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	private BigDecimal calculateYearlyInterest(BigDecimal baseProfit, int month) {
		BigDecimal principal;
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal interest;
		BigDecimal profit = baseProfit;
		for (int i = 1; i <= month; i++) {
			principal = profit.add(investmentAmount.getAmount().getValue());
			interest = interestRate.getMonthlyRate().multiply(principal);
			profit = principal.add(interest);
			result = result.add(interest);
		}
		return result;
	}
}
