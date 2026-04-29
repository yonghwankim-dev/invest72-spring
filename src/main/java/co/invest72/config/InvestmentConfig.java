package co.invest72.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.financial_product.infrastructure.mapper.ProductAmountMapper;
import co.invest72.investment.application.CalculateInvestment;
import co.invest72.investment.application.InvestmentFactory;
import co.invest72.investment.application.TaxFormatter;
import co.invest72.investment.application.TaxPercentFormatter;
import co.invest72.money.infrastructure.MoneyMapper;

@Configuration
public class InvestmentConfig {

	@Bean
	public CalculateInvestment calculateMonthlyInvestment(TaxFormatter taxFormatter, MoneyMapper moneyMapper) {
		return new CalculateInvestment(taxFormatter, moneyMapper);
	}

	@Bean
	public InvestmentFactory investmentFactory(ProductAmountMapper productAmountMapper,
		ExchangeRateRepository exchangeRateRepository) {
		return new InvestmentFactory(productAmountMapper, exchangeRateRepository);
	}

	@Bean
	public TaxPercentFormatter taxPercentFormatter() {
		return new TaxPercentFormatter();
	}
}
