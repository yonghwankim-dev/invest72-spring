package co.invest72.investment.presentation.response;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CalculateInvestmentResponse {
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
	public CalculateInvestmentResponse(int totalInvestment, int totalPrincipal, int totalInterest, int totalTax,
		int totalProfit, String taxType, String taxPercent, List<MonthlyInvestmentResult> monthlyDetails,
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

	@Override
	public String toString() {
		String header = String.format("%-10s %-15s %-15s %-15s%n",
			"회차", "원금", "이자", "수익");
		String body = String.format("총 원금: %,d원, 총 이자: %,d원, 총 세금: %,d원, 총 수익 금액: %,d원%n",
			totalPrincipal, totalInterest, totalTax, totalProfit);
		return header + body;
	}
}
