package co.invest72.investment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.investment.application.CalculateExpirationInvestment;
import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.application.TaxFormatter;
import co.invest72.investment.application.TaxPercentFormatter;

@Configuration
public class SpringConfig {
	@Bean
	public CalculateExpirationInvestment calculateExpirationInvestment(InvestmentFactory factory,
		TaxFormatter taxFormatter) {
		return new CalculateExpirationInvestment(factory, taxFormatter);
	}

	@Bean
	public CalculateInvestment calculateMonthlyInvestment(InvestmentFactory factory, TaxFormatter taxFormatter) {
		return new CalculateInvestment(factory, taxFormatter);
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
