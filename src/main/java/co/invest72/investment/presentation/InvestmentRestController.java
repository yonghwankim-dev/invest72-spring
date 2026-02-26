package co.invest72.investment.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
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

	@PostMapping("/investments/calculate/monthly")
	public ResponseEntity<CalculateMonthlyInvestmentResponse> calculateMonthly(
		@Valid @RequestBody CalculateInvestmentRequest request) {
		CalculateMonthlyInvestmentResponse response = calculateInvestment.calMonthlyInvestmentAmount(
			request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/investments/calculate/yearly")
	public ResponseEntity<CalculateYearlyInvestmentResponse> calculateYearly(
		@Valid @RequestBody CalculateInvestmentRequest request) {
		CalculateYearlyInvestmentResponse response = calculateInvestment.calYearlyInvestmentAmount(request);
		return ResponseEntity.ok(response);
	}
}
