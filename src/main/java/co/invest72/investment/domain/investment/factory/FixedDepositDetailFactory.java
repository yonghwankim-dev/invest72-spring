package co.invest72.investment.domain.investment.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.InvestmentAmount;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentDetail;
import co.invest72.money.domain.Money;
import lombok.Builder;

@Builder(toBuilder = true)
public class FixedDepositDetailFactory {
	private final InvestmentAmount investmentAmount;
	private final InterestRate interestRate;
	private final InvestPeriod investPeriod;
	private final InterestType interestType;

	public FixedDepositDetailFactory(InvestmentAmount investmentAmount, InterestRate interestRate,
		InvestPeriod investPeriod, InterestType interestType) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
		this.interestType = interestType;
	}

	public List<InvestmentDetail> createMonthlyDetails() {
		return createDetailsByPeriod(1, investPeriod.getMonths());
	}

	public List<InvestmentDetail> createYearlyDetails() {
		return createDetailsByPeriod(12, getFinalYear());
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	private List<InvestmentDetail> createDetailsByPeriod(int monthsInPeriod, int totalIterations) {
		List<InvestmentDetail> result = new ArrayList<>();

		Money originalAmount = investmentAmount.getAmount();
		Money principal = originalAmount;
		Money interest = originalAmount.times(BigDecimal.ZERO);
		Money profit = originalAmount;

		result.add(new InvestmentDetail(0, principal, interest, profit));

		for (int i = 1; i <= totalIterations; i++) {
			principal = profit;

			// 해당 주기에 포함될 개월 수 계산 (마지막 주기의 자투리 개월 처리)
			int months = calculateActualMonths(i, monthsInPeriod, totalIterations);

			// 한 달치 이자에 해당 주기의 개월 수를 곱함
			interest = interestType.calculateInterest(originalAmount, principal, interestRate)
				.times(BigDecimal.valueOf(months));

			profit = principal.add(interest);
			result.add(new InvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}

	private int calculateActualMonths(int currentIteration, int monthsInPeriod, int totalIterations) {
		// 월별(1)인 경우는 계산할 필요 없이 항상 1
		if (monthsInPeriod == 1) {
			return 1;
		}
		// 마지막 루프인 경우, 남은 자투리 개월수 계산
		if (currentIteration == totalIterations) {
			return investPeriod.getMonths() - (currentIteration - 1) * monthsInPeriod;
		}
		// 그 외 일반적인 주기(예: 1~(n-1)년차)는 설정된 주기(12) 반환
		return monthsInPeriod;
	}
}
