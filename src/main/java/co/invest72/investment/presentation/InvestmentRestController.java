package co.invest72.investment.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.investment.application.CalculateExpirationInvestment;
import co.invest72.investment.application.CalculateMonthlyCompoundInterest;
import co.invest72.investment.application.CalculateMonthlyInvestment;
import co.invest72.investment.application.dto.CalculateMonthlyCompoundInterestDto;
import co.invest72.investment.application.dto.CalculateMonthlyCompoundInterestResultDto;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
import co.invest72.investment.presentation.request.MonthlyCompoundInterestCalculateRequest;
import co.invest72.investment.presentation.response.CalculateMonthlyCompoundInterestResponse;
import co.invest72.investment.presentation.response.CalculateMonthlyInvestmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InvestmentRestController {

	private final CalculateExpirationInvestment calculateExpirationInvestment;
	private final CalculateMonthlyInvestment calculateMonthlyInvestment;
	private final CalculateMonthlyCompoundInterest calculateMonthlyCompoundInterest;

	@PostMapping("/investments/calculate/expiration")
	public ResponseEntity<CalculateExpirationInvestment.CalculateExpirationInvestmentResponse> calculateExpiration(
		@Valid @RequestBody CalculateInvestmentRequest request) {
		CalculateExpirationInvestment.CalculateExpirationInvestmentResponse response = calculateExpirationInvestment.calInvestment(
			request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/investments/calculate/monthly")
	public ResponseEntity<CalculateMonthlyInvestmentResponse> calculateMonthly(
		@RequestParam(required = false, defaultValue = "monthly") String view,
		@Valid @RequestBody CalculateInvestmentRequest request) {
		log.info("View type: {}, request : {}", view, request);
		CalculateMonthlyInvestmentResponse response = calculateMonthlyInvestment.calMonthlyInvestmentAmount(
			request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/investments/calculate/monthly-compound-interest")
	public ResponseEntity<CalculateMonthlyCompoundInterestResponse> calculateMonthlyCompoundInterest(
		@Valid @RequestBody MonthlyCompoundInterestCalculateRequest request) {
		CalculateMonthlyCompoundInterestDto dto = CalculateMonthlyCompoundInterestDto.builder()
			.initialAmount(request.getInitialAmount())
			.monthlyDeposit(request.getMonthlyDeposit())
			.investmentYears(request.getInvestmentYears())
			.annualInterestRate(request.getAnnualInterestRate())
			.compoundingMethod(request.getCompoundingMethod())
			.build();

		CalculateMonthlyCompoundInterestResultDto resultDto = calculateMonthlyCompoundInterest.calculate(dto);

		CalculateMonthlyCompoundInterestResponse response = CalculateMonthlyCompoundInterestResponse.builder()
			.totalInvestment(resultDto.getTotalInvestment())
			.totalInterest(resultDto.getTotalInterest())
			.totalProfit(resultDto.getTotalProfit())
			.details(resultDto.getDetails())
			.build();
		return ResponseEntity.ok(response);
	}
}
