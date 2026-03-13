package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.MonthlyInvestmentDetail;

public class SimpleFixedDepositMonthlyDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public SimpleFixedDepositMonthlyDetailFactory(InvestmentAmount investmentAmount, InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<MonthlyInvestmentDetail> createDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();

		BigDecimal principal = investmentAmount.getAmountMoney().getValue();
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = investmentAmount.getAmountMoney().getValue();
		result.add(new MonthlyInvestmentDetail(0, principal, interest, profit));

		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			principal = profit;
			interest = interestRate.getMonthlyRate().multiply(investmentAmount.getAmountMoney().getValue());
			profit = principal.add(interest);
			result.add(new MonthlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}
}
