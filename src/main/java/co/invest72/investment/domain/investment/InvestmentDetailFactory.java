package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

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
		IntFunction<Integer> monthCalculator = year -> 1;
		DetailCreator<MonthlyInvestmentDetail> supplier = (index, principal, interest, profit) -> MonthlyInvestmentDetail.builder()
			.month(index)
			.principal(principal)
			.interest(interest)
			.profit(profit)
			.build();
		return createDetails(investPeriod.getMonths(), monthCalculator, supplier);
	}

	private <T> List<T> createDetails(int finalPeriod, IntFunction<Integer> monthCalculator, DetailCreator<T> creator) {
		List<T> result = new ArrayList<>();
		BigDecimal principal = investmentAmount.getAmount();
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = investmentAmount.getAmount();
		result.add(creator.create(0, principal, interest, profit));
		for (int i = 1; i <= finalPeriod; i++) {
			principal = profit;
			int month = monthCalculator.apply(i);
			interest = interestRate.calMonthlyInterest(investmentAmount.getAmount())
				.multiply(BigDecimal.valueOf(month));
			profit = principal.add(interest);
			result.add(creator.create(i, principal, interest, profit));
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
			interest = interestRate.calMonthlyInterest(investmentAmount.getAmount())
				.multiply(BigDecimal.valueOf(monthsInYear));
			profit = principal.add(interest);
			result.add(YearlyInvestmentDetail.builder()
				.year(i)
				.principal(principal)
				.interest(interest)
				.profit(profit)
				.build()
			);
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

	// 람다 가독성을 위한 커스텀 함수형 인터페이스
	@FunctionalInterface
	private interface DetailCreator<T> {
		T create(int index, BigDecimal principal, BigDecimal interest, BigDecimal profit);
	}
}
