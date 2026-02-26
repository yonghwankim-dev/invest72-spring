package co.invest72.financial_product.presentation.dto.response;

import java.util.List;

import co.invest72.investment.presentation.response.MonthlyInvestmentResult;
import co.invest72.investment.presentation.response.YearlyInvestmentResult;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FinancialProductCalculationResponseDto {
	private final int totalInvestment;
	private final int totalPrincipal;
	private final int totalInterest;
	private final int totalTax;
	private final int totalProfit;
	private final String taxType;
	private final String taxPercent;
	private final List<MonthlyInvestmentResult> monthlyDetails;
	private final List<YearlyInvestmentResult> yearlyDetails;

	@Builder
	public FinancialProductCalculationResponseDto(int totalInvestment, int totalPrincipal, int totalInterest,
		int totalTax, int totalProfit, String taxType, String taxPercent, List<MonthlyInvestmentResult> monthlyDetails,
		List<YearlyInvestmentResult> yearlyDetails) {
		this.totalInvestment = totalInvestment;
		this.totalPrincipal = totalPrincipal;
		this.totalInterest = totalInterest;
		this.totalTax = totalTax;
		this.totalProfit = totalProfit;
		this.taxType = taxType;
		this.taxPercent = taxPercent;
		this.monthlyDetails = monthlyDetails;
		this.yearlyDetails = yearlyDetails;
	}
}
