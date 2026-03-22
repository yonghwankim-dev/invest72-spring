package co.invest72.investment.presentation.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CalculateInvestmentResponse {
	private final BigDecimal totalInvestment;
	private final BigDecimal totalInterest;
	private final BigDecimal totalTax;
	private final BigDecimal totalProfit;
	private final String taxType;
	private final String taxPercent;
	private final List<MonthlyInvestmentResult> monthlyDetails;
	private final List<YearlyInvestmentResult> yearlyDetails;
	private final InvestmentCurrency investmentCurrency;

	@Builder
	public CalculateInvestmentResponse(BigDecimal totalInvestment, BigDecimal totalInterest, BigDecimal totalTax,
		BigDecimal totalProfit, String taxType, String taxPercent, List<MonthlyInvestmentResult> monthlyDetails,
		List<YearlyInvestmentResult> yearlyDetails, InvestmentCurrency investmentCurrency) {
		this.totalInvestment = Objects.requireNonNull(totalInvestment, "총 투자 금액은 null이면 안됩니다.");
		this.totalInterest = Objects.requireNonNull(totalInterest, "총 이자는 null이면 안됩니다.");
		this.totalTax = Objects.requireNonNull(totalTax, "총 세금은 null이면 안됩니다.");
		this.totalProfit = Objects.requireNonNull(totalProfit, "총 수익금액은 null이면 안됩니다.");
		this.taxType = Objects.requireNonNull(taxType, "세금 종류는 null이면 안됩니다.");
		this.taxPercent = Objects.requireNonNull(taxPercent, "세율은 null이면 안됩니다.");
		this.monthlyDetails = Objects.requireNonNull(monthlyDetails, "월별 수익 리스트는 null이면 안됩니다.");
		this.yearlyDetails = Objects.requireNonNull(yearlyDetails, "년간 수익 리스트는 null이면 안됩니다.");
		this.investmentCurrency = Objects.requireNonNull(investmentCurrency, "통화는 null이면 안됩니다.");
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
			", investmentCurrency=" + investmentCurrency +
			'}';
	}
}
