package co.invest72.investment.presentation.response;

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
	private final int principal;
	private final int interest;
	private final int profit;
}
