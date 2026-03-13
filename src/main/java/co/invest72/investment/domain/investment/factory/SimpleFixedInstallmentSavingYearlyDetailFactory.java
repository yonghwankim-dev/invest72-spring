package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.investment.YearlyInvestmentDetail;
import co.invest72.money.domain.Money;

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
		Money principal = Money.won(BigDecimal.ZERO);
		Money interest = Money.won(BigDecimal.ZERO);
		Money profit = Money.won(BigDecimal.ZERO);

		// 0년차 초기값
		result.add(new YearlyInvestmentDetail(0, principal.getValue(), interest.getValue(), profit.getValue()));

		for (int year = 1; year <= getFinalYear(); year++) {
			int monthsInYear = calculateMonthsInYear(year);

			// 1. 기초 원금 (이전 연도까지 쌓인 순수 투자 원금)
			Money openingPrincipal = investmentAmount.getAmount()
				.times(BigDecimal.valueOf((year - 1) * 12L));

			// 2. principal : 당해 연도 누적 원금 (이전 총액 + 당해 신규 적립금)
			// 샘플 데이터 구조상 profit(이월총액) + 당해 적립금
			Money yearlyContribution = investmentAmount.getAmount()
				.times(BigDecimal.valueOf(monthsInYear));
			principal = profit.add(yearlyContribution);

			// 3. interest : 당해 발생 이자 (monthsInYear 반영)
			// a: 기존 누적 원금에 대해 이번 연도 경과 개월수만큼 붙는 이자
			Money a = interestRate.calMonthlyInterest(openingPrincipal)
				.times(BigDecimal.valueOf(monthsInYear));

			// b: 이번 연도에 새로 적립하는 금액들에 대한 단리 적금 이자
			// 등차수열 공식을 이용하여 monthsInYear에 따라 가변적으로 계산
			long sumOfMonths = (long)monthsInYear * (monthsInYear + 1) / 2;
			Money b = interestRate.calMonthlyInterest(investmentAmount.getAmount())
				.times(BigDecimal.valueOf(sumOfMonths));

			interest = a.add(b);

			profit = principal.add(interest);
			result.add(new YearlyInvestmentDetail(year, principal.getValue(), interest.getValue(), profit.getValue()));
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
