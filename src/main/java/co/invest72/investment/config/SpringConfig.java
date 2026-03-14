package co.invest72.investment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.financial_product.infrastructure.mapper.ProductAmountMapper;
import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.application.TaxFormatter;
import co.invest72.investment.application.TaxPercentFormatter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {
	
	@Bean
	public CalculateInvestment calculateMonthlyInvestment(TaxFormatter taxFormatter) {
		return new CalculateInvestment(taxFormatter);
	}

	@Bean
	public InvestmentFactory investmentFactory(ProductAmountMapper productAmountMapper) {
		return new InvestmentFactory(productAmountMapper);
	}

	@Bean
	public TaxPercentFormatter taxPercentFormatter() {
		return new TaxPercentFormatter();
	}
}
