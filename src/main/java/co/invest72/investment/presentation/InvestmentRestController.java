package co.invest72.investment.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.investment.application.CalculateExpirationInvestment;
import co.invest72.investment.application.CalculateMonthlyInvestment;
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

	private final CalculateExpirationInvestment calculateExpirationInvestment;
	private final CalculateMonthlyInvestment calculateMonthlyInvestment;

	@PostMapping("/investments/calculate/expiration")
	public ResponseEntity<CalculateExpirationInvestment.CalculateExpirationInvestmentResponse> calculateExpiration(
		@Valid @RequestBody CalculateInvestmentRequest request) {
		CalculateExpirationInvestment.CalculateExpirationInvestmentResponse response = calculateExpirationInvestment.calInvestment(
			request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/investments/calculate/monthly")
	public ResponseEntity<CalculateMonthlyInvestmentResponse> calculateMonthly(
		@Valid @RequestBody CalculateInvestmentRequest request) {
		CalculateMonthlyInvestmentResponse response = calculateMonthlyInvestment.calMonthlyInvestmentAmount(
			request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/investments/calculate/yearly")
	public ResponseEntity<CalculateYearlyInvestmentResponse> calculateYearly(
		@Valid @RequestBody CalculateInvestmentRequest request) {
		CalculateYearlyInvestmentResponse response = calculateMonthlyInvestment.calYearlyInvestmentAmount(request);
		return ResponseEntity.ok(response);
	}
}
