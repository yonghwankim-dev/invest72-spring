package co.invest72.investment.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InvestmentController {

	@GetMapping("/fixed-deposit")
	public String showExpirationCalculationPage() {
		return "fixed-deposit";
	}

	@GetMapping("/fixed-installment")
	public String showMonthlyCalculationPage() {
		return "fixed-installment";
	}

	@GetMapping("/investments/calculate/result")
	public String showMonthlyResultPage() {
		return "table-result";
	}
}
