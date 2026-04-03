package co.invest72.investment.domain.investment;

import java.util.Objects;

import co.invest72.money.domain.Money;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class YearlyInvestmentDetail {
	private final int year;
	private final Money principal;
	private final Money interest;
	private final Money profit;

	@Builder
	public YearlyInvestmentDetail(int year, Money principal, Money interest, Money profit) {
		this.year = year;
		this.principal = Objects.requireNonNull(principal);
		this.interest = Objects.requireNonNull(interest);
		this.profit = Objects.requireNonNull(profit);
	}
}
