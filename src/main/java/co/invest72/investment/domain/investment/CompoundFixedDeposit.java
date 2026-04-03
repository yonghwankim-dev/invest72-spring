package co.invest72.investment.domain.investment;

import java.math.BigDecimal;
import java.util.List;

import co.invest72.investment.domain.InterestRate;
import co.invest72.investment.domain.InvestPeriod;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.domain.LumpSumInvestmentAmount;
import co.invest72.investment.domain.Taxable;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.factory.FixedDepositDetailFactory;
import co.invest72.investment.domain.investment.factory.InvestmentDetailFactory;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;
import lombok.Builder;

public class CompoundFixedDeposit implements Investment {

	private final LumpSumInvestmentAmount investmentAmount;
	private final InvestPeriod investPeriod;
	private final InterestRate interestRate;
	private final Taxable taxable;
	private final List<InvestmentDetail> details;
	private final List<InvestmentDetail> yearlyDetails;

	@Builder(toBuilder = true)
	public CompoundFixedDeposit(LumpSumInvestmentAmount investmentAmount, InvestPeriod investPeriod,
		InterestRate interestRate,
		Taxable taxable) {
		this.investmentAmount = investmentAmount;
		this.investPeriod = investPeriod;
		this.interestRate = interestRate;
		this.taxable = taxable;
		InvestmentDetailFactory factory = new FixedDepositDetailFactory(investmentAmount, interestRate, investPeriod,
			InterestType.COMPOUND);
		this.details = factory.createMonthlyDetails();
		this.yearlyDetails = factory.createYearlyDetails();
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
		Money principal = details.get(month).getPrincipal();
		return roundToWholeMoney.apply(principal);
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
		return roundToWholeMoney.apply(details.get(month).getInterest());
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
		return roundToWholeMoney.apply(details.get(month).getProfit());
	}

	@Override
	public Money getTotalInvestment() {
		return roundToWholeMoney.apply(investmentAmount.getAmount());
	}

	@Override
	public Money getTotalInterest() {
		Money totalInterest = details.stream()
			.skip(1)
			.map(InvestmentDetail::getInterest)
			.reduce(Money::add)
			.orElseGet(() -> Money.of(BigDecimal.ZERO, investmentAmount.getAmount().getCurrency()));
		return roundToWholeMoney.apply(totalInterest);
	}

	@Override
	public Money getTotalTax() {
		Money tax = taxable.applyTax(getTotalInterest());
		return roundToWholeMoney.apply(tax);
	}

	@Override
	public Money getTotalProfit() {
		Money totalTax = getTotalTax();
		Money profit = details.get(getFinalMonth()).getProfit();
		Money totalProfit = profit.subtract(totalTax);
		return roundToWholeMoney.apply(totalProfit);
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
		return roundToWholeMoney.apply(yearlyDetails.get(year).getPrincipal());
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
		return roundToWholeMoney.apply(yearlyDetails.get(year).getInterest());
	}

	@Override
	public Money getProfitForYear(int year) {
		int finalYear = getFinalYear();
		if (year > finalYear) {
			return getProfitForYear(finalYear);
		}
		if (year < 0) {
			return getProfitForYear(0);
		}
		Money profit = yearlyDetails.get(year).getProfit();
		return roundToWholeMoney.apply(profit);
	}

	@Override
	public BigDecimal getTaxRate() {
		return taxable.getTaxRate();
	}

	@Override
	public Currency getCurrency() {
		return investmentAmount.getAmount().getCurrency();
	}
}
