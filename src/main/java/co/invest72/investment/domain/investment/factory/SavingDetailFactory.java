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

	@Override
	public List<InvestmentDetail> createYearlyDetails() {
		// 월별 기초 데이터 생성
		List<InvestmentDetail> monthlyDetails = generateMonthlyBaseDetails();
		List<InvestmentDetail> yearlyDetails = new ArrayList<>();

		// 0년차 (초기 데이터)
		yearlyDetails.add(monthlyDetails.get(0));
		for (int year = 1; year <= getFinalYear(); year++) {
			int startMonth = getStartMonthBy(year);
			int endMonth = getEndMonthBy(year);

			// 해당 연도 구간(start ~ end)에 발생한 이자만 모두 합산
			Money yearlyInterestSum = investmentAmount.getAmount().times(BigDecimal.ZERO);
			for (int m = startMonth; m <= endMonth; m++) {
				yearlyInterestSum = yearlyInterestSum.add(monthlyDetails.get(m).getInterest());
			}

			// 해당 연도 말의 최종 원리금
			Money yearlyProfit = monthlyDetails.get(endMonth).getProfit();
			yearlyDetails.add(new InvestmentDetail(
				year,
				yearlyProfit.subtract(yearlyInterestSum),
				yearlyInterestSum,
				yearlyProfit
			));
		}
		return yearlyDetails;
	}

	private int getStartMonthBy(int year) {
		return (year - 1) * 12 + 1;
	}

	private int getEndMonthBy(int year) {
		int totalMonths = investPeriod.getMonths();
		return Math.min(year * 12, totalMonths);
	}

	private int getFinalYear() {
		if (investPeriod.getMonths() == 0) {
			return 0;
		}
		return (investPeriod.getMonths() - 1) / 12 + 1;
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
