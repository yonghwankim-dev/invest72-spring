package co.invest72.investment.domain;

import java.math.BigDecimal;

import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Money;

public class CashInvestment implements Investment {

	private final InvestmentAmount investmentAmount;

	public CashInvestment(InvestmentAmount investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

	@Override
	public BigDecimal getPrincipal() {
		return getPrincipal(getFinalMonth()).getValue();
	}

	@Override
	public Money getPrincipal(int month) {
		return roundToWholeMoney.apply(investmentAmount.getAmount());
	}

	@Override
	public BigDecimal getInterest() {
		return getInterest(getFinalMonth());
	}

	@Override
	public BigDecimal getInterest(int month) {
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getProfit() {
		return getProfit(getFinalMonth());
	}

	@Override
	public BigDecimal getProfit(int month) {
		return roundToWholeMoney.apply(investmentAmount.getAmount()).getValue();
	}

	@Override
	public BigDecimal getTotalInvestment() {
		return roundToWholeMoney.apply(investmentAmount.getAmount()).getValue();
	}

	@Override
	public BigDecimal getTotalInterest() {
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getTotalTax() {
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getTotalProfit() {
		return roundToWholeMoney.apply(investmentAmount.getAmount()).getValue();
	}

	@Override
	public int getFinalMonth() {
		return 0;
	}

	@Override
	public String getTaxType() {
		return TaxType.NONE.getDescription();
	}

	@Override
	public BigDecimal getPrincipalForYear(int year) {
		return roundToWholeMoney.apply(investmentAmount.getAmount()).getValue();
	}

	@Override
	public BigDecimal getInterestForYear(int year) {
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getProfitForYear(int year) {
		return roundToWholeMoney.apply(investmentAmount.getAmount()).getValue();
	}

	@Override
	public BigDecimal getTaxRate() {
		return BigDecimal.ZERO;
	}
}
