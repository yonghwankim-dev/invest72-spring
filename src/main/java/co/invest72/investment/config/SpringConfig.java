package co.invest72.investment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.application.TaxFormatter;
import co.invest72.investment.application.TaxPercentFormatter;

@Configuration
public class SpringConfig {

	@Bean
	public CalculateInvestment calculateMonthlyInvestment(TaxFormatter taxFormatter) {
		return new CalculateInvestment(taxFormatter);
	}

	@Bean
	public InvestmentFactory investmentFactory() {
		return new InvestmentFactory();
	}

	@Bean
	public TaxPercentFormatter taxPercentFormatter() {
		return new TaxPercentFormatter();
	}
}
