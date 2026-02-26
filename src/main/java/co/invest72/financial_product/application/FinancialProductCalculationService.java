package co.invest72.financial_product.application;

import org.springframework.stereotype.Service;

import co.invest72.financial_product.presentation.dto.response.FinancialProductCalculationResponseDto;
import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.presentation.response.CalculateMonthlyInvestmentResponse;
import co.invest72.investment.presentation.response.CalculateYearlyInvestmentResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialProductCalculationService {

	private final CalculateInvestment calculateMonthlyInvestment;

	public FinancialProductCalculationResponseDto calculate(Investment investment) {
		CalculateMonthlyInvestmentResponse monthlyResponse = calculateMonthlyInvestment.calMonthlyInvestment(
			investment);
		CalculateYearlyInvestmentResponse yearlyResponse = calculateMonthlyInvestment.calYearlyInvestment(
			investment);

		return FinancialProductCalculationResponseDto.builder()
			.totalInvestment(monthlyResponse.getTotalInvestment())
			.totalPrincipal(monthlyResponse.getTotalPrincipal())
			.totalInterest(monthlyResponse.getTotalInterest())
			.totalTax(monthlyResponse.getTotalTax())
			.totalProfit(monthlyResponse.getTotalProfit())
			.taxType(monthlyResponse.getTaxType())
			.taxPercent(monthlyResponse.getTaxPercent())
			.monthlyDetails(monthlyResponse.getDetails())
			.yearlyDetails(yearlyResponse.getDetails())
			.build();
	}
}
