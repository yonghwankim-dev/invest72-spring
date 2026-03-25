package co.invest72.financial_product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.financial_product.domain.service.FinancialProductCalculator;

@Configuration
public class FinancialProductSpringConfig {

	@Bean
	public FinancialProductCalculator financialProductCalculator() {
		return new FinancialProductCalculator();
	}
}
