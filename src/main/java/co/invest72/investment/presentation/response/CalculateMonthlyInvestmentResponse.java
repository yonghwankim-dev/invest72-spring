package co.invest72.investment.presentation.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CalculateMonthlyInvestmentResponse {
	private final BigDecimal totalInvestment;
	private final BigDecimal totalInterest;
	private final BigDecimal totalTax;
	private final BigDecimal totalProfit;
	private final String taxType;
	private final String taxPercent;
	private final List<MonthlyInvestmentResult> details;

	@Builder
	public CalculateMonthlyInvestmentResponse(BigDecimal totalInvestment, BigDecimal totalInterest,
		BigDecimal totalTax, BigDecimal totalProfit, String taxType, String taxPercent,
		List<MonthlyInvestmentResult> details) {
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
		return "CalculateMonthlyInvestmentResponse{" +
			"totalInvestment=" + totalInvestment +
			", totalInterest=" + totalInterest +
			", totalTax=" + totalTax +
			", totalProfit=" + totalProfit +
			", taxType='" + taxType + '\'' +
			", taxPercent='" + taxPercent + '\'' +
			", details=" + details.stream()
			.map(MonthlyInvestmentResult::toString)
			.collect(Collectors.joining(", ")) +
			'}';
	}

}
