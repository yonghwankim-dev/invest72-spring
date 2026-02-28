package co.invest72.investment.domain;

import java.math.BigDecimal;

import co.invest72.investment.domain.tax.TaxType;

public class CashInvestment implements Investment {

	private InvestmentAmount investmentAmount;

	public CashInvestment(InvestmentAmount investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

	@Override
	public int getPrincipal() {
		return getPrincipal(getFinalMonth());
	}

	@Override
	public int getPrincipal(int month) {
		return roundToInt.applyAsInt(investmentAmount.getAmount());
	}

	@Override
	public int getInterest() {
		return getInterest(getFinalMonth());
	}

	@Override
	public int getInterest(int month) {
		return 0;
	}

	@Override
	public int getProfit() {
		return getProfit(getFinalMonth());
	}

	@Override
	public int getProfit(int month) {
		return roundToInt.applyAsInt(investmentAmount.getAmount());
	}

	@Override
	public BigDecimal getTotalInvestment() {
		return investmentAmount.getAmount();
	}

	@Override
	public int getTotalInterest() {
		return 0;
	}

	@Override
	public int getTotalTax() {
		return 0;
	}

	@Override
	public int getTotalProfit() {
		return roundToInt.applyAsInt(investmentAmount.getAmount());
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
	public int getPrincipalForYear(int year) {
		return roundToInt.applyAsInt(investmentAmount.getAmount());
	}

	@Override
	public int getInterestForYear(int year) {
		return 0;
	}

	@Override
	public int getProfitForYear(int year) {
		return roundToInt.applyAsInt(investmentAmount.getAmount());
	}

	@Override
	public double getTaxRate() {
		return 0.0;
	}
}
