package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.invest72.investment.application.dto.MonthlyInvestmentDetail;
import co.invest72.investment.application.dto.YearlyInvestmentDetail;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.investment.domain.Taxable;
import lombok.Builder;

/**
 * 정기 예금
 * 단리로 이자를 계산하며, 세금이 적용됩니다.
 */
public class SimpleFixedDeposit implements Investment {

	private final LumpSumInvestmentAmount investmentAmount;
	private final InvestPeriod investPeriod;
	private final InterestRate interestRate;
	private final Taxable taxable;
	private final List<MonthlyInvestmentDetail> details;
	private final List<YearlyInvestmentDetail> yearlyDetails;

	@Builder(toBuilder = true)
	public SimpleFixedDeposit(LumpSumInvestmentAmount investmentAmount, InvestPeriod investPeriod,
		InterestRate interestRate, Taxable taxable) {
		this.investmentAmount = investmentAmount;
		this.investPeriod = investPeriod;
		this.interestRate = interestRate;
		this.taxable = taxable;
		this.details = calculateDetails();
		this.yearlyDetails = calculateYearlyDetails();
	}

	private List<MonthlyInvestmentDetail> calculateDetails() {
		List<MonthlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = investmentAmount.getAmount();
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal tax = BigDecimal.ZERO;
		BigDecimal profit = investmentAmount.getAmount();
		result.add(new MonthlyInvestmentDetail(0, principal, interest, tax, profit));
		for (int i = 1; i <= getFinalMonth(); i++) {
			principal = profit;
			interest = interestRate.getMonthlyRate().multiply(investmentAmount.getAmount());
			tax = taxable.applyTax(interest);
			profit = principal.add(interest);
			result.add(new MonthlyInvestmentDetail(i, principal, interest, tax, profit));
		}
		return result;
	}

	private List<YearlyInvestmentDetail> calculateYearlyDetails() {
		List<YearlyInvestmentDetail> result = new ArrayList<>();
		BigDecimal principal = investmentAmount.getAmount();
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal tax = BigDecimal.ZERO;
		BigDecimal profit = investmentAmount.getAmount();
		result.add(new YearlyInvestmentDetail(0, principal, interest, tax, profit));
		int finalYear = getFinalYear();
		// getFinalMonth()=13, finalYear=2
		for (int i = 1; i <= finalYear; i++) {
			principal = profit;
			// 마지막 해의 경우, 남은 개월 수에 따라 이자를 계산합니다.
			int monthsInYear = Math.min(12, getFinalMonth() - (i - 1) * 12);
			interest = interestRate.getMonthlyRate()
				.multiply(investmentAmount.getAmount())
				.multiply(BigDecimal.valueOf(monthsInYear));
			tax = taxable.applyTax(interest);
			profit = principal.add(interest).subtract(tax);
			result.add(new YearlyInvestmentDetail(i, principal, interest, tax, profit));
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
		return getInterest(getFinalMonth());
	}

	@Override
	public int getAccInterest() {
		return getAccInterest(getFinalMonth());
	}

	@Override
	public int getAccInterest(int month) {
		if (month > getFinalMonth()) {
			return getAccInterest();
		}
		if (month < 0) {
			return getAccInterest(0);
		}

		BigDecimal result = details.stream()
			.skip(1)
			.limit(month) // Include months from 0 to the specified month
			.map(MonthlyInvestmentDetail::getInterest)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return roundToInt.applyAsInt(result);
	}

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

	/**
	 * 만기 시점의 총 원금 금액을 반환합니다.
	 * <p>
	 * 단리 방식의 예금은 원금이 변하지 않으므로, 단순히 초기 투자 금액을 반환합니다.
	 * </p>
	 * @return 초기 원금 금액
	 */
	@Override
	public int getTotalPrincipal() {
		return getPrincipal();
	}

	@Override
	public int getTotalInterest() {
		BigDecimal totalInterest = details.stream()
			.skip(1) // 0월은 이자가 없음
			.map(MonthlyInvestmentDetail::getInterest)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return roundToInt.applyAsInt(totalInterest);
	}

	@Override
	public int getTotalTax() {
		BigDecimal totalTax = details.stream()
			.skip(1) // 첫 번째 항목(0월)은 세금이 없으므로 건너뜁니다.
			.map(MonthlyInvestmentDetail::getTax)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return roundToInt.applyAsInt(totalTax);
	}

	@Override
	public int getTotalProfit() {
		BigDecimal principal = details.get(getFinalMonth()).getPrincipal();
		BigDecimal interest = details.get(getFinalMonth()).getInterest();
		BigDecimal tax = BigDecimal.valueOf(getTotalTax());
		return roundToInt.applyAsInt(principal.add(interest).subtract(tax));
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
		return roundToInt.applyAsInt(yearlyDetails.get(year).getInterest());
	}
}
