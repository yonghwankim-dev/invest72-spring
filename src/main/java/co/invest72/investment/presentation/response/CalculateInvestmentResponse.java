package co.invest72.investment.presentation.response;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CalculateInvestmentResponse {
	private final int totalInvestment;
	private final int totalInterest;
	private final int totalTax;
	private final int totalProfit;
	private final String taxType;
	private final String taxPercent;
	private final List<MonthlyInvestmentResult> monthlyDetails;
	private final List<YearlyInvestmentResult> yearlyDetails;

	@Builder
	public CalculateInvestmentResponse(int totalInvestment, int totalInterest, int totalTax,
		int totalProfit, String taxType, String taxPercent, List<MonthlyInvestmentResult> monthlyDetails,
		List<YearlyInvestmentResult> yearlyDetails) {
		this.totalInvestment = totalInvestment;
		this.totalInterest = totalInterest;
		this.totalTax = totalTax;
		this.totalProfit = totalProfit;
		this.taxType = taxType;
		this.taxPercent = taxPercent;
		this.monthlyDetails = monthlyDetails;
		this.yearlyDetails = yearlyDetails;
	}

	@Override
	public String toString() {
		return "CalculateInvestmentResponse{" +
			"totalInvestment=" + totalInvestment +
			", totalInterest=" + totalInterest +
			", totalTax=" + totalTax +
			", totalProfit=" + totalProfit +
			", taxType='" + taxType + '\'' +
			", taxPercent='" + taxPercent + '\'' +
			", monthlyDetails=" + monthlyDetails +
			", yearlyDetails=" + yearlyDetails +
			'}';
	}
}
