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
	public Money getPrincipal() {
		return getPrincipal(getFinalMonth());
	}

	@Override
	public Money getPrincipal(int month) {
		return roundToWholeMoney.apply(investmentAmount.getAmount());
	}

	@Override
	public Money getInterest() {
		return getInterest(getFinalMonth());
	}

	@Override
	public Money getInterest(int month) {
		return Money.of(BigDecimal.ZERO, investmentAmount.getAmount().getCurrency());
	}

	@Override
	public Money getProfit() {
		return getProfit(getFinalMonth());
	}

	@Override
	public Money getProfit(int month) {
		return roundToWholeMoney.apply(investmentAmount.getAmount());
	}

	@Override
	public Money getTotalInvestment() {
		return roundToWholeMoney.apply(investmentAmount.getAmount());
	}

	@Override
	public Money getTotalInterest() {
		return Money.of(BigDecimal.ZERO, investmentAmount.getAmount().getCurrency());
	}

	@Override
	public Money getTotalTax() {
		return Money.of(BigDecimal.ZERO, investmentAmount.getAmount().getCurrency());
	}

	@Override
	public BigDecimal getTotalProfit() {
		return roundToWholeMoney.apply(investmentAmount.getAmount()).getValue();
	}

	@Override
	public Money getTotalProfitMoney() {
		return roundToWholeMoney.apply(investmentAmount.getAmount());
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
