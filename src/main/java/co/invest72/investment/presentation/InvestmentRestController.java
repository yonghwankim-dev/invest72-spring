package co.invest72.investment.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.presentation.request.CalculateInvestmentRequest;
import co.invest72.investment.presentation.response.CalculateInvestmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InvestmentRestController {

	private final CalculateInvestment calculateInvestment;
	private final InvestmentFactory investmentFactory;

	@PostMapping("/investments/calculate")
	public ResponseEntity<CalculateInvestmentResponse> calculate(
		@Valid @RequestBody CalculateInvestmentRequest request) {
		Investment investment = investmentFactory.createBy(request);
		CalculateInvestmentResponse response = calculateInvestment.calculate(investment);
		return ResponseEntity.ok(response);
	}
}
