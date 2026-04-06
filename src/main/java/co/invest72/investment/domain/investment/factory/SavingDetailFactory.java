package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentDetail;
import co.invest72.money.domain.Money;
import lombok.Builder;

@Builder(toBuilder = true)
public class SavingDetailFactory implements InvestmentDetailFactory {
	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;
	private final InterestType interestType;

	public SavingDetailFactory(InvestmentAmount investmentAmount, InterestRate interestRate, InvestPeriod investPeriod,
		InterestType interestType) {
		this.investmentAmount = Objects.requireNonNull(investmentAmount);
		this.interestRate = Objects.requireNonNull(interestRate);
		this.investPeriod = Objects.requireNonNull(investPeriod);
		this.interestType = Objects.requireNonNull(interestType);
	}

	@Override
	public List<InvestmentDetail> createMonthlyDetails() {
		return generateMonthlyBaseDetails();
	}

	// TODO: 년도별 데이터 생성 구현
	@Override
	public List<InvestmentDetail> createYearlyDetails() {
		List<InvestmentDetail> monthlyDetails = generateMonthlyBaseDetails();
		List<InvestmentDetail> result = new ArrayList<>();
		if (interestType == InterestType.SIMPLE) {
			result = createSimpleYearlyDetails();
		} else if (interestType == InterestType.COMPOUND) {
			result = createCompoundYearlyDetails();
		}
		return result;
	}

	private List<InvestmentDetail> createCompoundYearlyDetails() {
		List<InvestmentDetail> result = new ArrayList<>();
		Money principal = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money interest = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money profit = investmentAmount.getAmount().times(BigDecimal.ZERO);

		result.add(new InvestmentDetail(0, principal, interest, profit));
		int years = getFinalYear();
		for (int i = 1; i <= years; i++) {
			BigDecimal months = BigDecimal.valueOf(Math.min(12, investPeriod.getMonths() - (i - 1) * 12));
			Money value = investmentAmount.getAmount().times(months);
			principal = profit.add(value);

			interest = calculateYearlyInterest(profit, months.intValue());

			profit = principal.add(interest);
			result.add(new InvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}

	private Money calculateYearlyInterest(Money baseProfit, int month) {
		Money principal;
		Money result = baseProfit.times(BigDecimal.ZERO);
		Money interest;
		Money profit = baseProfit;
		for (int i = 1; i <= month; i++) {
			principal = profit.add(investmentAmount.getAmount());
			interest = interestRate.calMonthlyInterest(principal);
			profit = principal.add(interest);
			result = result.add(interest);
		}
		return result;
	}

	private List<InvestmentDetail> createSimpleYearlyDetails() {
		List<InvestmentDetail> result = new ArrayList<>();
		Money principal = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money interest = investmentAmount.getAmount().times(BigDecimal.ZERO);
		Money profit = investmentAmount.getAmount().times(BigDecimal.ZERO);

		// 0년차 초기값
		result.add(new InvestmentDetail(0, principal, interest, profit));

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
			result.add(new InvestmentDetail(year, principal, interest, profit));
		}
		return result;
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	private int calculateMonthsInYear(int currentYear) {
		return Math.min(12, investPeriod.getMonths() - (currentYear - 1) * 12);
	}

	private List<InvestmentDetail> generateMonthlyBaseDetails() {
		List<InvestmentDetail> result = new ArrayList<>();
		Money originalAmount = investmentAmount.getAmount();
		Money accInvestmentAmount = originalAmount.times(BigDecimal.ZERO);
		Money principal = originalAmount.times(BigDecimal.ZERO);
		Money interest = originalAmount.times(BigDecimal.ZERO);
		Money profit = originalAmount.times(BigDecimal.ZERO);

		result.add(new InvestmentDetail(0, principal, interest, profit));
		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			accInvestmentAmount = accInvestmentAmount.add(originalAmount);
			principal = profit.add(originalAmount);
			interest = interestType.calculateInterest(accInvestmentAmount, principal, interestRate);
			profit = principal.add(interest);
			result.add(new InvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}
}
