package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.YearlyInvestmentDetail;
import co.invest72.money.domain.Money;

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

		Money principal = investmentAmount.getAmount();
		Money interest = Money.won(BigDecimal.ZERO);
		Money profit = investmentAmount.getAmount();
		result.add(new YearlyInvestmentDetail(0, principal.getValue(), interest.getValue(), profit.getValue()));

		for (int i = 1; i <= getFinalYear(); i++) {
			principal = profit;
			BigDecimal months = BigDecimal.valueOf(calculateMonthsInYear(i));
			interest = interestRate.calMonthlyInterest(investmentAmount.getAmount()).times(months);
			profit = principal.add(interest);
			result.add(new YearlyInvestmentDetail(i, principal.getValue(), interest.getValue(), profit.getValue()));
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
