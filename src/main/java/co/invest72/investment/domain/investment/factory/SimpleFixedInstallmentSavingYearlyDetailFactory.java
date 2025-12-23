package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.YearlyInvestmentDetail;

public class SimpleFixedInstallmentSavingYearlyDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public SimpleFixedInstallmentSavingYearlyDetailFactory(InvestmentAmount investmentAmount,
		InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<YearlyInvestmentDetail> createDetails() {
		List<YearlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal accInvestmentAmount = BigDecimal.ZERO;
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;

		result.add(new YearlyInvestmentDetail(0, principal, interest, profit));
		int years = getFinalYear();
		for (int i = 1; i <= years; i++) {
			BigDecimal value = investmentAmount.getAmount().multiply(BigDecimal.valueOf(12));
			accInvestmentAmount = accInvestmentAmount.add(value);
			principal = profit.add(value);

			interest = calculateYearlyInterest(i * 12);

			profit = principal.add(interest);
			result.add(new YearlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	private BigDecimal calculateYearlyInterest(int months) {
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal accInvestmentAmount = BigDecimal.ZERO;
		BigDecimal principal;
		BigDecimal interest;
		BigDecimal profit = BigDecimal.ZERO;
		for (int i = 1; i <= months; i++) {
			accInvestmentAmount = accInvestmentAmount.add(investmentAmount.getAmount());
			principal = profit.add(investmentAmount.getAmount());
			interest = interestRate.getMonthlyRate().multiply(accInvestmentAmount);
			profit = principal.add(interest);

			result = result.add(interest);
		}
		return result;
	}
}
