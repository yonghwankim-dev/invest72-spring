package co.invest72.investment.application.dto;

import java.util.List;

import co.invest72.investment.domain.investment.MonthlyInvestmentDetail;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CalculateMonthlyCompoundInterestResultDto {
	private final Integer totalInvestment;
	private final Integer totalInterest;
	private final Integer totalProfit;
	private final List<MonthlyInvestmentDetail> details;
}
