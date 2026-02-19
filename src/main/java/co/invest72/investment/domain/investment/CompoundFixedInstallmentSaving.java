package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.List;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.investment.factory.CompoundFixedInstallmentSavingMonthlyDetailFactory;
import co.invest72.investment.domain.investment.factory.CompoundFixedInstallmentSavingYearlyDetailFactory;
import lombok.Builder;

public class CompoundFixedInstallmentSaving implements Investment {

	private final InstallmentInvestmentAmount investmentAmount;
	private final InvestPeriod investPeriod;
	private final InterestRate interestRate;
	private final Taxable taxable;
	private final List<MonthlyInvestmentDetail> details;
	private final List<YearlyInvestmentDetail> yearlyDetails;

	@Builder(toBuilder = true)
	public CompoundFixedInstallmentSaving(InstallmentInvestmentAmount investmentAmount, InvestPeriod investPeriod,
		InterestRate interestRate, Taxable taxable) {
		this.investmentAmount = investmentAmount;
		this.investPeriod = investPeriod;
		this.interestRate = interestRate;
		this.taxable = taxable;
		CompoundFixedInstallmentSavingMonthlyDetailFactory factory = new CompoundFixedInstallmentSavingMonthlyDetailFactory(
			investmentAmount, interestRate, investPeriod);
		this.details = factory.createDetails();
		CompoundFixedInstallmentSavingYearlyDetailFactory yearlyFactory = new CompoundFixedInstallmentSavingYearlyDetailFactory(
			investmentAmount, interestRate, investPeriod);
		this.yearlyDetails = yearlyFactory.createDetails();
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
		BigDecimal totalInvestment = investmentAmount.getAmount()
			.multiply(BigDecimal.valueOf(investPeriod.getMonths()));
		return roundToInt.applyAsInt(totalInvestment);
	}

	@Override
	public int getTotalPrincipal() {
		return roundToInt.applyAsInt(details.get(getFinalMonth()).getPrincipal());
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
		BigDecimal principal = details.get(getFinalMonth()).getPrincipal();
		BigDecimal interest = details.get(getFinalMonth()).getInterest();
		BigDecimal totalTax = BigDecimal.valueOf(getTotalTax());
		return roundToInt.applyAsInt(principal.add(interest).subtract(totalTax));
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
