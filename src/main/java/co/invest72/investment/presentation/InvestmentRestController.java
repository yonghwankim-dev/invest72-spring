package co.invest72.investment.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
import co.invest72.investment.presentation.response.CalculateInvestmentResponse;
import co.invest72.investment.presentation.response.CalculateMonthlyInvestmentResponse;
import co.invest72.investment.presentation.response.CalculateYearlyInvestmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InvestmentRestController {

	private final CalculateInvestment calculateInvestment;

	@PostMapping("/investments/calculate")
	public ResponseEntity<CalculateInvestmentResponse> calculate(
		@Valid @RequestBody CalculateInvestmentRequest request) {
		CalculateMonthlyInvestmentResponse monthlyResponse = calculateInvestment.calMonthlyInvestmentAmount(request);
		CalculateYearlyInvestmentResponse yearlyResponse = calculateInvestment.calYearlyInvestmentAmount(request);

		CalculateInvestmentResponse response = CalculateInvestmentResponse.builder()
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
		return ResponseEntity.ok(response);
	}
}
