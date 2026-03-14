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
import co.invest72.money.domain.Money;
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
		return getInterest(investPeriod.getMonths());
	}

	/**
	 * 지정된 월 회차(month)까지의 누적 이자 금액을 반환합니다.
	 *
	 * @param month 회차 (1부터 시작)
	 * @return 이자 금액=원금×(1+월이자율)^개월수−원금
	 */
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
			.skip(1)
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
		BigDecimal totalTax = getTotalTax().getValue();
		BigDecimal totalProfit = details.get(getFinalMonth()).getProfit().subtract(totalTax);
		Money profitMoney = Money.of(totalProfit, investmentAmount.getAmount().getCurrency());
		return roundToWholeMoney.apply(profitMoney);
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
