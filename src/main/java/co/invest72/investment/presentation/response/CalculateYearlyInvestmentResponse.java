package co.invest72.investment.presentation.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CalculateYearlyInvestmentResponse {
	private final BigDecimal totalInvestment;
	private final BigDecimal totalInterest;
	private final BigDecimal totalTax;
	private final BigDecimal totalProfit;
	private final String taxType;
	private final String taxPercent;
	private final List<YearlyInvestmentResult> details;

	@Builder
	public CalculateYearlyInvestmentResponse(BigDecimal totalInvestment, BigDecimal totalInterest,
		BigDecimal totalTax, BigDecimal totalProfit, String taxType, String taxPercent,
		List<YearlyInvestmentResult> details) {
		this.totalInvestment = totalInvestment;
		this.totalInterest = totalInterest;
		this.totalTax = totalTax;
		this.totalProfit = totalProfit;
		this.taxType = taxType;
		this.taxPercent = taxPercent;
		this.details = details;
	}

	@Override
	public String toString() {
		return "CalculateYearlyInvestmentResponse{" +
			"totalInvestment=" + totalInvestment +
			", totalInterest=" + totalInterest +
			", totalTax=" + totalTax +
			", totalProfit=" + totalProfit +
			", taxType='" + taxType + '\'' +
			", taxPercent='" + taxPercent + '\'' +
			", details=" + details.stream()
			.map(YearlyInvestmentResult::toString)
			.collect(Collectors.joining(", ")) +
			'}';
	}

}
