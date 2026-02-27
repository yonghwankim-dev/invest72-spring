package co.invest72.financial_product.application;

import org.springframework.stereotype.Service;

import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.domain.Investment;
import co.invest72.investment.presentation.response.CalculateInvestmentResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialProductCalculationService {

	private final CalculateInvestment calculateInvestment;

	public CalculateInvestmentResponse calculate(Investment investment) {
		return calculateInvestment.calculate(investment);
	}
}
