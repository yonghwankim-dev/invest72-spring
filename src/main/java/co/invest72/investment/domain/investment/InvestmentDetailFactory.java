package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;

public class InvestmentDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public InvestmentDetailFactory(InvestmentAmount investmentAmount, InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<MonthlyInvestmentDetail> createMonthlyDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = investmentAmount.getAmount();
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = investmentAmount.getAmount();
		result.add(new MonthlyInvestmentDetail(0, principal, interest, profit));
		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			principal = profit;
			interest = interestRate.getMonthlyRate()
				.multiply(investmentAmount.getAmount());
			profit = principal.add(interest);
			result.add(new MonthlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}

	public List<YearlyInvestmentDetail> calculateYearlyDetails() {
		List<YearlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = investmentAmount.getAmount();
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = investmentAmount.getAmount();

		result.add(new YearlyInvestmentDetail(0, principal, interest, profit));
		for (int i = 1; i <= getFinalYear(); i++) {
			principal = profit;
			int monthsInYear = calculateMonthsInYear(i);
			interest = interestRate.getMonthlyRate()
				.multiply(investmentAmount.getAmount())
				.multiply(BigDecimal.valueOf(monthsInYear));
			profit = principal.add(interest);
			result.add(new YearlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	// 해당 연도의 남은 개월 수를 계산합니다.
	private int calculateMonthsInYear(int currentYear) {
		return Math.min(12, investPeriod.getMonths() - (currentYear - 1) * 12);
	}
}
