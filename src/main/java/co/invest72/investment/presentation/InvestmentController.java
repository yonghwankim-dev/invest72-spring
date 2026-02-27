package co.invest72.investment.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InvestmentController {

	@GetMapping("/investments/deposit")
	public String showExpirationCalculationPage() {
		return "deposit";
	}

	@GetMapping("/investments/savings")
	public String showMonthlyCalculationPage() {
		return "savings";
	}

	@GetMapping("/investments/calculate/result")
	public String showMonthlyResultPage() {
		return "calculate-result";
	}
}
