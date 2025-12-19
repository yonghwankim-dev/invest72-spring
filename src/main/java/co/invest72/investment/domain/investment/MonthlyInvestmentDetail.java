package co.invest72.investment.domain.investment;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MonthlyInvestmentDetail {
	private final int month;
	private final BigDecimal principal;
	private final BigDecimal interest;
	private final BigDecimal profit;

	@Builder
	public MonthlyInvestmentDetail(int month, BigDecimal principal, BigDecimal interest, BigDecimal profit) {
		this.month = month;
		this.principal = principal;
		this.interest = interest;
		this.profit = profit;
	}
}
