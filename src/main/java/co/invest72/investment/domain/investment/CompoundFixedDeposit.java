package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.factory.FixedDepositDetailFactory;
import lombok.Builder;

public class CompoundFixedDeposit implements Investment {

	private final LumpSumInvestmentAmount investmentAmount;
	private final InvestPeriod investPeriod;
	private final InterestRate interestRate;
	private final Taxable taxable;
	private final List<MonthlyInvestmentDetail> details;
	private final List<YearlyInvestmentDetail> yearlyDetails;

	@Builder(toBuilder = true)
	public CompoundFixedDeposit(LumpSumInvestmentAmount investmentAmount, InvestPeriod investPeriod,
		InterestRate interestRate,
		Taxable taxable) {
		this.investmentAmount = investmentAmount;
		this.interestRate = interestRate;
		this.investPeriod = investPeriod;
		this.taxable = taxable;
		FixedDepositDetailFactory factory = new FixedDepositDetailFactory(investmentAmount, interestRate, investPeriod);
		this.details = factory.createMonthlyDetails(InterestType.COMPOUND);
		this.yearlyDetails = factory.createYearlyDetails(InterestType.COMPOUND);
	}

	private List<MonthlyInvestmentDetail> calculateDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = investmentAmount.getAmount(); // 초기 원금
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal profit = investmentAmount.getAmount(); // 0 월의 총합은 원금과 동일
		// 0 월
		result.add(new MonthlyInvestmentDetail(0, principal, interest, profit));

		for (int i = 1; i <= getFinalMonth(); i++) {
			principal = profit; // 다음 달의 원금은 이번 달의 총합

			// 월 이자 계산
			interest = interestRate.getMonthlyRate().multiply(principal);

			// 복리 예금: 원금에 이자가 합산되고 세금은 차감
			profit = principal.add(interest);

			result.add(new MonthlyInvestmentDetail(i, principal, interest, profit));
		}
		return result;
	}

	@Override
	public int getPrincipal() {
		return getPrincipal(getFinalMonth());
	}

	@Override
	public int getPrincipal(int month) {
		if (month > getFinalMonth()) {
			return getPrincipal();
		}
		if (month < 0) {
			return getPrincipal(0);
		}
		return roundToInt.applyAsInt(details.get(month).getPrincipal());
	}

	@Override
	public int getInterest() {
		return getInterest(investPeriod.getMonths());
	}

	/**
	 * 지정된 월 회차(month)까지의 누적 이자 금액을 반환합니다.
	 *
	 * @param month 회차 (1부터 시작)
	 * @return 이자 금액=원금×(1+월이자율)^개월수−원금
	 */
	@Override
	public int getInterest(int month) {
		if (month > getFinalMonth()) {
			return getInterest();
		}
		if (month < 0) {
			return getInterest(0);
		}
		return roundToInt.applyAsInt(details.get(month).getInterest());
	}

	@Override
	public int getProfit() {
		return getProfit(getFinalMonth());
	}

	@Override
	public int getProfit(int month) {
		if (month > getFinalMonth()) {
			return getProfit();
		}
		if (month < 0) {
			return getProfit(0);
		}
		return roundToInt.applyAsInt(details.get(month).getProfit());
	}

	@Override
	public int getTotalInvestment() {
		return roundToInt.applyAsInt(investmentAmount.getAmount());
	}

	@Override
	public int getTotalPrincipal() {
		return getPrincipal();
	}

	@Override
	public int getTotalInterest() {
		BigDecimal totalInterest = details.stream()
			.skip(1)
			.map(MonthlyInvestmentDetail::getInterest)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return roundToInt.applyAsInt(totalInterest);
	}

	@Override
	public int getTotalTax() {
		return taxable.applyTax(getTotalInterest());
	}

	@Override
	public int getTotalProfit() {
		BigDecimal totalTax = BigDecimal.valueOf(getTotalTax());
		BigDecimal totalProfit = details.get(getFinalMonth()).getProfit().subtract(totalTax);
		return roundToInt.applyAsInt(totalProfit);
	}

	@Override
	public int getFinalMonth() {
		return investPeriod.getMonths();
	}

	@Override
	public String getTaxType() {
		return taxable.getTaxType();
	}

	@Override
	public int getPrincipalForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getPrincipalForYear(finalYear);
		}
		if (year < 0) {
			return getPrincipalForYear(0);
		}
		return roundToInt.applyAsInt(yearlyDetails.get(year).getPrincipal());
	}

	private int getFinalYear() {
		return (getFinalMonth() - 1) / 12 + 1;
	}

	@Override
	public int getInterestForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getInterestForYear(finalYear);
		}
		if (year < 0) {
			return getInterestForYear(0);
		}
		return roundToInt.applyAsInt(yearlyDetails.get(year).getInterest());
	}

	@Override
	public int getProfitForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getProfitForYear(finalYear);
		}
		if (year < 0) {
			return getProfitForYear(0);
		}
		return roundToInt.applyAsInt(yearlyDetails.get(year).getProfit());
	}
}
