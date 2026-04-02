package co.invest72.investment.domain.investment;

import co.invest72.money.domain.Money;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class MonthlyInvestmentDetail {
	private final int month;
	private final Money principal;
	private final Money interest;
	private final Money profit;

	@Builder
	public MonthlyInvestmentDetail(int month, Money principal, Money interest, Money profit) {
		this.month = month;
		this.principal = principal;
		this.interest = interest;
		this.profit = profit;
	}
}
