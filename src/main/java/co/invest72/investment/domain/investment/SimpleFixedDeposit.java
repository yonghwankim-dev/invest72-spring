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
		Money interest = Money.of(value, investmentAmount.getAmount().getCurrency());
		return roundToWholeMoney.apply(interest);
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
		return roundToWholeMoney.apply(investmentAmount.getAmount());
	}

	@Override
	public Money getTotalInterest() {
		BigDecimal totalInterest = details.stream()
			.skip(1) // 0월은 이자가 없음
			.map(MonthlyInvestmentDetail::getInterest)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		Money money = Money.of(totalInterest, investmentAmount.getAmount().getCurrency());
		return roundToWholeMoney.apply(money);
	}

	@Override
	public Money getTotalTax() {
		BigDecimal tax = taxable.applyTax(getTotalInterest().getValue());
		Money taxMoney = Money.of(tax, investmentAmount.getAmount().getCurrency());
		return roundToWholeMoney.apply(taxMoney);
	}

	@Override
	public Money getTotalProfit() {
		BigDecimal principal = details.get(getFinalMonth()).getPrincipal();
		BigDecimal interest = details.get(getFinalMonth()).getInterest();
		BigDecimal tax = getTotalTax().getValue();
		BigDecimal totalProfit = principal.add(interest).subtract(tax);
		Money totalProfitMoney = Money.of(totalProfit, investmentAmount.getAmount().getCurrency());
		return roundToWholeMoney.apply(totalProfitMoney);
	}

	@Override
	public int getFinalMonth() {
		return investPeriod.getMonths();
	}

	@Override
	public String getTaxType() {
		return taxable.getTaxType();
	}

	private int getFinalYear() {
		return (getFinalMonth() - 1) / 12 + 1;
	}

	@Override
	public Money getPrincipalForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getPrincipalForYear(finalYear);
		}
		if (year < 0) {
			return getPrincipalForYear(0);
		}
		BigDecimal principal = yearlyDetails.get(year).getPrincipal();
		Money principalMoney = Money.of(principal, investmentAmount.getAmount().getCurrency());
		return roundToWholeMoney.apply(principalMoney);
	}

	@Override
	public Money getInterestForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getInterestForYear(finalYear);
		}
		if (year < 0) {
			return getInterestForYear(0);
		}
		BigDecimal interest = yearlyDetails.get(year).getInterest();
		Money interestMoney = Money.of(interest, investmentAmount.getAmount().getCurrency());
		return roundToWholeMoney.apply(interestMoney);
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
