package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.interest.InterestType;

public class FixedInstallmentSavingDetailFactory {

	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;

	public FixedInstallmentSavingDetailFactory(InvestmentAmount investmentAmount, InterestRate interestRate,
		InvestPeriod investPeriod) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
	}

	public List<MonthlyInvestmentDetail> createMonthlyDetails(InterestType interestType) {
		IntFunction<Integer> monthCalculator = year -> 1;
		DetailCreator<MonthlyInvestmentDetail> supplier = (index, principal, interest, profit) -> MonthlyInvestmentDetail.builder()
			.month(index)
			.principal(principal)
			.interest(interest)
			.profit(profit)
			.build();
		InterestCalculator interestCalculator = (rate, amount, months) -> rate.calMonthlyInterest(amount)
			.multiply(months);
		return createDetails(investPeriod.getMonths(), monthCalculator, supplier, interestType,
			interestCalculator);
	}

	public List<YearlyInvestmentDetail> createYearlyDetails(InterestType interestType) {
		int finalYear = getFinalYear();
		IntFunction<Integer> monthCalculator = this::calculateMonthsInYear;
		DetailCreator<YearlyInvestmentDetail> creator = (index, principal, interest, profit) -> YearlyInvestmentDetail.builder()
			.year(index)
			.principal(principal)
			.interest(interest)
			.profit(profit)
			.build();
		InterestCalculator interestCalculator = createYearlyInterestCalculator(interestType);

		return createDetails(finalYear, monthCalculator, creator, interestType,
			interestCalculator);
	}

	private InterestCalculator createYearlyInterestCalculator(InterestType interestType) {
		InterestCalculator interestCalculator;
		if (interestType == InterestType.SIMPLE) {
			interestCalculator = (rate, amount, months) -> {
				BigDecimal investmentAmount = amount;
				BigDecimal accInterest = BigDecimal.ZERO;
				for (int i = 1; i <= months.intValue(); i++) {
					BigDecimal interest = interestRate.getMonthlyRate().multiply(investmentAmount);
					accInterest = accInterest.add(interest);
					investmentAmount = investmentAmount.add(amount);
				}
				return accInterest;
			};
			return interestCalculator;
		} else if (interestType == InterestType.COMPOUND) {
			interestCalculator = (rate, amount, months) -> {
				BigDecimal total = amount;
				BigDecimal growthFactor = rate.calGrowthFactor();
				for (int i = 0; i < months.intValue(); i++) {
					total = total.multiply(growthFactor);
				}
				return total.subtract(amount);
			};
			return interestCalculator;
		}
		throw new UnsupportedOperationException("지원하지 않는 이자 유형입니다: " + interestType);
	}

	// 해당 연도의 남은 개월 수를 계산합니다.
	private int calculateMonthsInYear(int currentYear) {
		return Math.min(12, investPeriod.getMonths() - (currentYear - 1) * 12);
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	private <T> List<T> createDetails(int finalPeriod, IntFunction<Integer> monthCalculator, DetailCreator<T> creator,
		InterestType interestType, InterestCalculator interestCalculator) {
		List<T> result = new ArrayList<>();

		BigDecimal investment = BigDecimal.ZERO;
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;
		result.add(creator.create(0, principal, interest, profit));

		for (int i = 1; i <= finalPeriod; i++) {
			int month = monthCalculator.apply(i);
			investment = investment.add(investmentAmount.getAmount());
			principal = profit.add(investmentAmount.getAmount())
				.multiply(BigDecimal.valueOf(month));

			if (interestType == InterestType.SIMPLE) {
				interest = interestCalculator.calculate(interestRate, investment,
					BigDecimal.valueOf(month));
			}

			profit = principal.add(interest);
			result.add(creator.create(i, principal, interest, profit));
		}
		return result;
	}

	private List<YearlyInvestmentDetail> calculateYearlyDetails() {
		List<YearlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = BigDecimal.ZERO;
		// 0 월
		result.add(new YearlyInvestmentDetail(0, principal, interest, profit));
		int finalMonth = investPeriod.getMonths();
		BigDecimal accInterest = BigDecimal.ZERO;
		for (int i = 1; i <= finalMonth; i++) {
			principal = principal.add(investmentAmount.getAmount());
			interest = interestRate.getMonthlyRate().multiply(principal);

			accInterest = accInterest.add(interest);
			if (i % 12 == 0) {
				BigDecimal yearlyProfit = principal.add(accInterest);
				result.add(new YearlyInvestmentDetail(i / 12, principal, accInterest, yearlyProfit));
			}
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
