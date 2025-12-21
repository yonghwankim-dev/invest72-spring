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
		InterestCalculator interestCalculator = (rate, amount, months) -> rate.calMonthlyInterest(amount)
			.multiply(months);
		DetailCreator<MonthlyInvestmentDetail> supplier = (index, principal, interest, profit) -> MonthlyInvestmentDetail.builder()
			.month(index)
			.principal(principal)
			.interest(interest)
			.profit(profit)
			.build();
		return createDetails(investPeriod.getMonths(), monthCalculator, interestCalculator, supplier);
	}

	public List<YearlyInvestmentDetail> calculateYearlyDetails() {
		int finalYear = getFinalYear();
		InterestCalculator interestCalculator = (rate, amount, months) -> rate.calMonthlyInterest(amount)
			.multiply(months);
		IntFunction<Integer> monthCalculator = this::calculateMonthsInYear;
		DetailCreator<YearlyInvestmentDetail> creator = (index, principal, interest, profit) -> YearlyInvestmentDetail.builder()
			.year(index)
			.principal(principal)
			.interest(interest)
			.profit(profit)
			.build();
		return createDetails(finalYear, monthCalculator, interestCalculator, creator);
	}

	// 해당 연도의 남은 개월 수를 계산합니다.
	private int calculateMonthsInYear(int currentYear) {
		return Math.min(12, investPeriod.getMonths() - (currentYear - 1) * 12);
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	private <T> List<T> createDetails(int finalPeriod, IntFunction<Integer> monthCalculator,
		InterestCalculator interestCalculator, DetailCreator<T> creator) {
		List<T> result = new ArrayList<>();
		BigDecimal principal = investmentAmount.getAmount();
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = investmentAmount.getAmount();
		result.add(creator.create(0, principal, interest, profit));
		for (int i = 1; i <= finalPeriod; i++) {
			principal = profit;
			int month = monthCalculator.apply(i);
			interest = interestCalculator.calculate(interestRate, investmentAmount.getAmount(),
				BigDecimal.valueOf(month));
			profit = principal.add(interest);
			result.add(creator.create(i, principal, interest, profit));
		}
		return result;
	}

	// 람다 가독성을 위한 커스텀 함수형 인터페이스
	@FunctionalInterface
	private interface DetailCreator<T> {
		T create(int index, BigDecimal principal, BigDecimal interest, BigDecimal profit);
	}

	@FunctionalInterface
	private interface InterestCalculator {
		BigDecimal calculate(InterestRate interestRate, BigDecimal amount, BigDecimal months);
	}
}
