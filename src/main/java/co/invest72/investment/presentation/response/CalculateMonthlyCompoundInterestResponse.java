package co.invest72.investment.presentation.response;

import java.util.List;

import co.invest72.investment.domain.investment.MonthlyInvestmentDetail;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CalculateMonthlyCompoundInterestResponse {
	private final Integer totalInvestment;
	private final Integer totalInterest;
	private final Integer totalProfit;
	private final List<MonthlyInvestmentDetail> details;

	@Builder
	private CalculateMonthlyCompoundInterestResponse(Integer totalInvestment, Integer totalInterest,
		Integer totalProfit, List<MonthlyInvestmentDetail> details) {
		this.totalInvestment = totalInvestment;
		this.totalInterest = totalInterest;
		this.totalProfit = totalProfit;
		this.details = details;
	}
}
