package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.investment.factory.CompoundFixedDepositMonthlyDetailFactory;
import co.invest72.investment.domain.investment.factory.CompoundFixedDepositYearlyDetailFactory;
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
		this.investPeriod = investPeriod;
		this.interestRate = interestRate;
		this.taxable = taxable;
		CompoundFixedDepositMonthlyDetailFactory factory = new CompoundFixedDepositMonthlyDetailFactory(
			investmentAmount, interestRate, investPeriod);
		this.details = factory.createDetails();
		CompoundFixedDepositYearlyDetailFactory yearlyFactory = new CompoundFixedDepositYearlyDetailFactory(
			investmentAmount, interestRate, investPeriod);
		this.yearlyDetails = yearlyFactory.createDetails();
	}

	@Override
	public BigDecimal getPrincipal() {
		return getPrincipal(getFinalMonth());
	}

	@Override
	public BigDecimal getPrincipal(int month) {
		if (month > getFinalMonth()) {
			return getPrincipal();
		}
		if (month < 0) {
			return getPrincipal(0);
		}
		return roundToWholeAmount.apply(details.get(month).getPrincipal());
	}

	@Override
	public BigDecimal getInterest() {
		return getInterest(investPeriod.getMonths());
	}

	/**
	 * 지정된 월 회차(month)까지의 누적 이자 금액을 반환합니다.
	 *
	 * @param month 회차 (1부터 시작)
	 * @return 이자 금액=원금×(1+월이자율)^개월수−원금
	 */
	@Override
	public BigDecimal getInterest(int month) {
		if (month > getFinalMonth()) {
			return getInterest();
		}
		if (month < 0) {
			return getInterest(0);
		}
		return roundToWholeAmount.apply(details.get(month).getInterest());
	}

	@Override
	public BigDecimal getProfit() {
		return getProfit(getFinalMonth());
	}

	@Override
	public BigDecimal getProfit(int month) {
		if (month > getFinalMonth()) {
			return getProfit();
		}
		if (month < 0) {
			return getProfit(0);
		}
		return roundToWholeAmount.apply(details.get(month).getProfit());
	}

	@Override
	public BigDecimal getTotalInvestment() {
		return roundToWholeAmount.apply(investmentAmount.getAmountMoney().getValue());
	}

	@Override
	public BigDecimal getTotalInterest() {
		BigDecimal totalInterest = details.stream()
			.skip(1)
			.map(MonthlyInvestmentDetail::getInterest)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return roundToWholeAmount.apply(totalInterest);
	}

	@Override
	public BigDecimal getTotalTax() {
		return roundToWholeAmount.apply(taxable.applyTax(getTotalInterest()));
	}

	@Override
	public BigDecimal getTotalProfit() {
		BigDecimal totalTax = getTotalTax();
		return roundToWholeAmount.apply(details.get(getFinalMonth()).getProfit().subtract(totalTax));
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
	public BigDecimal getPrincipalForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getPrincipalForYear(finalYear);
		}
		if (year < 0) {
			return getPrincipalForYear(0);
		}
		return roundToWholeAmount.apply(yearlyDetails.get(year).getPrincipal());
	}

	private int getFinalYear() {
		return (getFinalMonth() - 1) / 12 + 1;
	}

	@Override
	public BigDecimal getInterestForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getInterestForYear(finalYear);
		}
		if (year < 0) {
			return getInterestForYear(0);
		}
		return roundToWholeAmount.apply(yearlyDetails.get(year).getInterest());
	}

	@Override
	public BigDecimal getProfitForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getProfitForYear(finalYear);
		}
		if (year < 0) {
			return getProfitForYear(0);
		}
		return roundToWholeAmount.apply(yearlyDetails.get(year).getProfit());
	}

	@Override
	public BigDecimal getTaxRate() {
		return taxable.getTaxRate();
	}
}
