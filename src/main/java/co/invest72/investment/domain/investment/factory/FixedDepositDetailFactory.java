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
public class FixedDepositDetailFactory implements InvestmentDetailFactory {
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

	@Override
	public List<InvestmentDetail> createMonthlyDetails() {
		return generateMonthlyBaseDetails();
	}

	@Override
	public List<InvestmentDetail> createYearlyDetails() {
		List<InvestmentDetail> monthlyDetails = generateMonthlyBaseDetails();
		List<InvestmentDetail> yearlyDetails = new ArrayList<>();

		yearlyDetails.add(monthlyDetails.get(0));

		// 12개월 단위로 해당 시점의 월 복리 결과물들 추출
		for (int year = 1; year <= getFinalYear(); year++) {
			int startMonth = (year - 1) * 12 + 1;
			int endMonth = Math.min(year * 12, investPeriod.getMonths());

			// 투자기간이 0인 경우 처라
			if (endMonth < startMonth) {
				yearlyDetails.add(new InvestmentDetail(
					year,
					monthlyDetails.get(endMonth).getPrincipal(),
					monthlyDetails.get(endMonth).getInterest(),
					monthlyDetails.get(endMonth).getProfit()
				));
				break;
			}

			// 해당 연도의 시작 시 원금(그 해 1월의 기초 원금)
			Money yearlyPrincipal = monthlyDetails.get(startMonth).getPrincipal();
			// 해당 얀도의 발생한 이자 합산
			Money yearlyInterest = investmentAmount.getAmount().times(BigDecimal.ZERO);

			for (int m = startMonth; m <= endMonth; m++) {
				yearlyInterest = yearlyInterest.add(monthlyDetails.get(m).getInterest());
			}
			// 해당 연도 말의 최종 원리금
			Money yearlyProfit = monthlyDetails.get(endMonth).getProfit();
			yearlyDetails.add(new InvestmentDetail(
				year,
				yearlyPrincipal,
				yearlyInterest, // 12개월치 이자의 총합
				yearlyProfit
			));
		}
		return yearlyDetails;
	}

	private int getFinalYear() {
		return (investPeriod.getMonths() - 1) / 12 + 1;
	}

	private List<InvestmentDetail> generateMonthlyBaseDetails() {
		List<InvestmentDetail> result = new ArrayList<>();

		Money originalAmount = investmentAmount.getAmount();
		Money principal = originalAmount;
		Money interest = originalAmount.times(BigDecimal.ZERO);
		Money profit = originalAmount;

		result.add(new InvestmentDetail(0, principal, interest, profit));

		for (int i = 1; i <= investPeriod.getMonths(); i++) {
			principal = profit;

			interest = interestType.calculateInterest(originalAmount, principal, interestRate);

			profit = principal.add(interest);
			result.add(new InvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}
}
