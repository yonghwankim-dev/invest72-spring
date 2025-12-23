package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.YearlyInvestmentDetail;

public class SimpleFixedDepositYearlyDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public SimpleFixedDepositYearlyDetailFactory(InvestmentAmount investmentAmount, InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<YearlyInvestmentDetail> createDetails() {
		List<YearlyInvestmentDetail> result = new ArrayList<>();

		BigDecimal principal = investmentAmount.getAmount();
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = investmentAmount.getAmount();
		result.add(new YearlyInvestmentDetail(0, principal, interest, profit));

		for (int i = 1; i <= getFinalYear(); i++) {
			principal = profit;
			BigDecimal months = BigDecimal.valueOf(calculateMonthsInYear(i));
			interest = interestRate.getMonthlyRate().multiply(investmentAmount.getAmount())
				.multiply(months);
			profit = principal.add(interest);
			result.add(new YearlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	private int calculateMonthsInYear(int currentYear) {
		return Math.min(12, investPeriod.getMonths() - (currentYear - 1) * 12);
	}
}
