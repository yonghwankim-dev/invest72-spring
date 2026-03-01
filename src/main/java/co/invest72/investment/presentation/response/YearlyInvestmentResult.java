package co.invest72.investment.presentation.response;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class YearlyInvestmentResult {
	private final int year;
	private final BigDecimal principal;
	private final BigDecimal interest;
	private final BigDecimal profit;

	public YearlyInvestmentResult(int year, int principal, int interest, int profit) {
		this(year, BigDecimal.valueOf(principal), BigDecimal.valueOf(interest), BigDecimal.valueOf(profit));
	}
}
