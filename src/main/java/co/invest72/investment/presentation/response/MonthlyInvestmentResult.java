package co.invest72.investment.presentation.response;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class MonthlyInvestmentResult {
	private final int month;
	private final BigDecimal principal;
	private final BigDecimal interest;
	private final BigDecimal profit;

	public MonthlyInvestmentResult(int month, int principal, int interest, int profit) {
		this(month, BigDecimal.valueOf(principal), BigDecimal.valueOf(interest), BigDecimal.valueOf(profit));
	}

	public MonthlyInvestmentResult(int month, BigDecimal principal, BigDecimal interest, BigDecimal profit) {
		Objects.requireNonNull(principal, "원금은 null이면 안됩니다.");
		Objects.requireNonNull(interest, "이자는 null이면 안됩니다.");
		Objects.requireNonNull(profit, "수익은 null이면 안됩니다.");
		this.month = month;
		this.principal = principal;
		this.interest = interest;
		this.profit = profit;
	}
}
