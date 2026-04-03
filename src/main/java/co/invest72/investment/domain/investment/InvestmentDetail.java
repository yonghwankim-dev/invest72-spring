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
public class InvestmentDetail {
	private final int month;
	private final Money principal;
	private final Money interest;
	private final Money profit;

	@Builder
	public InvestmentDetail(int month, Money principal, Money interest, Money profit) {
		this.month = month;
		this.principal = Objects.requireNonNull(principal);
		this.interest = Objects.requireNonNull(interest);
		this.profit = Objects.requireNonNull(profit);
	}
}
