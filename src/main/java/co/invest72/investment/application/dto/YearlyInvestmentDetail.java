package co.invest72.investment.application.dto;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class YearlyInvestmentDetail {
	private final int year;
	private final BigDecimal principal;
	private final BigDecimal interest;
	private final BigDecimal profit;

	public YearlyInvestmentDetail(int year, BigDecimal principal, BigDecimal interest, BigDecimal profit) {
		this.year = year;
		this.principal = principal;
		this.interest = interest;
		this.profit = profit;
	}
}
