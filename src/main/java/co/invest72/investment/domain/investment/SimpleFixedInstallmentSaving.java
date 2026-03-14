package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.List;

import co.invest72.investment.domain.InstallmentInvestmentAmount;
import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.investment.factory.SimpleFixedInstallmentSavingMonthlyDetailFactory;
import co.invest72.investment.domain.investment.factory.SimpleFixedInstallmentSavingYearlyDetailFactory;
import co.invest72.money.domain.Money;
import lombok.Builder;

/**
 * 정기적금
 * 이자 계산 방식은 단리 방식으로, 매월 납입하는 금액에 대해 이자를 계산합니다.
 */
public class SimpleFixedInstallmentSaving implements Investment {

	private final InstallmentInvestmentAmount investmentAmount;
	private final InvestPeriod investPeriod;
	private final InterestRate interestRate;
	private final Taxable taxable;
	private final List<MonthlyInvestmentDetail> details;
	private final List<YearlyInvestmentDetail> yearlyDetails;

	@Builder(toBuilder = true)
	public SimpleFixedInstallmentSaving(InstallmentInvestmentAmount investmentAmount, InvestPeriod investPeriod,
		InterestRate interestRate, Taxable taxable) {
		this.investmentAmount = investmentAmount;
		this.investPeriod = investPeriod;
		this.interestRate = interestRate;
		this.taxable = taxable;
		SimpleFixedInstallmentSavingMonthlyDetailFactory factory = new SimpleFixedInstallmentSavingMonthlyDetailFactory(
			investmentAmount, interestRate, investPeriod);
		this.details = factory.createDetails();
		SimpleFixedInstallmentSavingYearlyDetailFactory yearlyFactory = new SimpleFixedInstallmentSavingYearlyDetailFactory(
			investmentAmount, interestRate, investPeriod);
		this.yearlyDetails = yearlyFactory.createDetails();
	}

	@Override
	public Money getPrincipal() {
		return getPrincipal(getFinalMonth());
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
	public Money getInterest() {
		return getInterest(getFinalMonth());
	}

	@Override
	public Money getInterest(int month) {
		if (month > getFinalMonth()) {
			return getInterest(getFinalMonth());
		}
		if (month < 0) {
			return getInterest(0);
		}
		BigDecimal value = details.get(month).getInterest();
		return roundToWholeMoney.apply(Money.of(value, investmentAmount.getAmount().getCurrency()));
	}

	@Override
	public Money getProfit() {
		return getProfit(getFinalMonth());
	}

	@Override
	public Money getProfit(int month) {
		if (month > getFinalMonth()) {
			return getProfit(getFinalMonth());
		}
		if (month < 0) {
			return getProfit(0);
		}
		BigDecimal value = details.get(month).getProfit();
		Money profit = Money.of(value, investmentAmount.getAmount().getCurrency());
		return roundToWholeMoney.apply(profit);
	}

	@Override
	public Money getTotalInvestment() {
		Money totalInvestment = investmentAmount.getAmount().times(investPeriod.getMonths());
		return roundToWholeMoney.apply(totalInvestment);
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
	public Money getTotalInterestMoney() {
		BigDecimal totalInterest = details.stream()
			.skip(1) // 0월은 이자가 없음
			.map(MonthlyInvestmentDetail::getInterest)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		Money money = Money.of(totalInterest, investmentAmount.getAmount().getCurrency());
		return roundToWholeMoney.apply(money);
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
