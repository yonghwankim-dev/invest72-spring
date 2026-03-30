package co.invest72.financial_product.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.CashProduct;
import co.invest72.financial_product.domain.DepositProduct;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.SavingsProduct;
import co.invest72.financial_product.domain.entity.FinancialProductData;
import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.investment.domain.investment.InvestmentType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FinancialProductFactory {

	private final LocalDateProvider localDateProvider;
	private final ProductIdGenerator idGenerator;

	public FinancialProduct create(FinancialProductData data) {
		String productId = idGenerator.generateId();
		LocalDateTime createdAt = localDateProvider.nowDateTime();
		FinancialProductData withedData = data
			.withProductId(productId)
			.withCreatedAt(createdAt);
		InvestmentType investmentType = InvestmentType.valueOf(withedData.getInvestmentType());

		return switch (investmentType) {
			case CASH -> new CashProduct(withedData);
			case DEPOSIT -> new DepositProduct(withedData);
			case SAVINGS -> new SavingsProduct(withedData);
		};
	}

	public FinancialProduct createUpdatedProduct(FinancialProduct origin, FinancialProductData data) {
		return origin.update(data);
	}
}
