package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.investment.factory.SimpleFixedDepositMonthlyDetailFactory;
import co.invest72.investment.domain.investment.factory.SimpleFixedDepositYearlyDetailFactory;
import co.invest72.money.domain.Money;
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
		SimpleFixedDepositMonthlyDetailFactory factory = new SimpleFixedDepositMonthlyDetailFactory(investmentAmount,
			interestRate, investPeriod);
		this.details = factory.createDetails();
		SimpleFixedDepositYearlyDetailFactory yearlyFactory = new SimpleFixedDepositYearlyDetailFactory(
			investmentAmount, interestRate, investPeriod);
		this.yearlyDetails = yearlyFactory.createDetails();
	}

	@Override
	public BigDecimal getPrincipal() {
		return getPrincipal(getFinalMonth()).getValue();
	}

	@Override
	public Money getPrincipal(int month) {
		if (month > getFinalMonth()) {
			return getPrincipal(getFinalMonth());
		}
		if (month < 0) {
			return getPrincipal(0);
		}
		BigDecimal principal = details.get(month).getPrincipal();
		return roundToWholeMoney.apply(Money.of(principal, investmentAmount.getAmount().getCurrency()));
	}

	@Override
	public BigDecimal getInterest() {
		return getInterest(getFinalMonth());
	}

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
		return roundToWholeMoney.apply(investmentAmount.getAmount()).getValue();
	}

	@Override
	public BigDecimal getTotalInterest() {
		BigDecimal totalInterest = details.stream()
			.skip(1) // 0월은 이자가 없음
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
		BigDecimal principal = details.get(getFinalMonth()).getPrincipal();
		BigDecimal interest = details.get(getFinalMonth()).getInterest();
		BigDecimal tax = getTotalTax();
		BigDecimal totalProfit = principal.add(interest).subtract(tax);
		return roundToWholeAmount.apply(totalProfit);
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
