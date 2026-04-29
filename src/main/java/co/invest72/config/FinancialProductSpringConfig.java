package co.invest72.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.financial_product.domain.service.FinancialProductCalculator;
import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.financial_product.infrastructure.mapper.ProductAmountMapper;

@Configuration
public class FinancialProductSpringConfig {

	@Bean
	public IdGenerator productIdGenerator() {
		return new ProductIdGenerator("product");
	}

	@Bean
	public FinancialProductCalculator financialProductCalculator() {
		return new FinancialProductCalculator();
	}

	@Bean
	public ProductAmountMapper productAmountMapper(ExchangeRateRepository exchangeRateRepository) {
		return new ProductAmountMapper(exchangeRateRepository);
	}
}
