package co.invest72.financial_product.presentation.dto.response;

import java.util.Objects;

import lombok.Getter;

@Getter
public class FinancialProductStatisticsResponse {
	private final TotalBalance totalBalance;
	private final TotalEstimatedInterest totalEstimatedInterest;

	public FinancialProductStatisticsResponse(TotalBalance totalBalance,
		TotalEstimatedInterest totalEstimatedInterest) {
		this.totalBalance = Objects.requireNonNull(totalBalance);
		this.totalEstimatedInterest = Objects.requireNonNull(totalEstimatedInterest);
	}
}
