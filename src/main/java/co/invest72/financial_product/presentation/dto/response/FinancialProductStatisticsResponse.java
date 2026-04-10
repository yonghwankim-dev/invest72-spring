package co.invest72.financial_product.presentation.dto.response;

import java.util.Objects;

import lombok.Getter;

@Getter
public class FinancialProductStatisticsResponse {
	private final TotalBalance totalBalance;

	public FinancialProductStatisticsResponse(TotalBalance totalBalance) {
		this.totalBalance = Objects.requireNonNull(totalBalance);
	}
}
