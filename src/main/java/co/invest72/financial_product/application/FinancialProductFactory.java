package co.invest72.financial_product.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.CashProduct;
import co.invest72.financial_product.domain.DepositProduct;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductInvestmentType;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductTaxRate;
import co.invest72.financial_product.domain.ProductTaxType;
import co.invest72.financial_product.domain.SavingsProduct;
import co.invest72.financial_product.domain.entity.FinancialProductData;
import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.PaymentDay;
import co.invest72.money.domain.Money;
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
		return toEntity(withedData);
	}

	public FinancialProduct toEntity(FinancialProductData data) {
		InvestmentType investmentType = InvestmentType.valueOf(data.getInvestmentType());

		return switch (investmentType) {
			case CASH -> cash(data);
			case DEPOSIT -> deposit(data);
			case SAVINGS -> savings(data);
		};
	}

	private FinancialProduct cash(FinancialProductData data) {
		return CashProduct.builder()
			.id(data.getProductId().orElseThrow())
			.userId(data.getUserId().orElseThrow())
			.name(data.getName())
			.productInvestmentType(ProductInvestmentType.from(data.getInvestmentType()))
			.amount(ProductAmount.from(Money.of(data.getAmount(), data.getCurrencyCode())))
			.months(new ProductMonths(data.getMonths()))
			.productAnnualInterestRate(new ProductAnnualInterestRate(data.getInterestRate()))
			.productInterestType(ProductInterestType.from(data.getInterestType()))
			.productTaxType(ProductTaxType.from(data.getTaxType()))
			.productTaxRate(new ProductTaxRate(data.getTaxRate()))
			.startDate(data.getStartDate())
			.createdAt(data.getCreatedAt().orElseThrow())
			.build();
	}

	private FinancialProduct deposit(FinancialProductData data) {
		return DepositProduct.builder()
			.id(data.getProductId().orElseThrow())
			.userId(data.getUserId().orElseThrow())
			.name(data.getName())
			.productInvestmentType(ProductInvestmentType.from(data.getInvestmentType()))
			.amount(ProductAmount.from(Money.of(data.getAmount(), data.getCurrencyCode())))
			.months(new ProductMonths(data.getMonths()))
			.productAnnualInterestRate(new ProductAnnualInterestRate(data.getInterestRate()))
			.productInterestType(ProductInterestType.from(data.getInterestType()))
			.productTaxType(ProductTaxType.from(data.getTaxType()))
			.productTaxRate(new ProductTaxRate(data.getTaxRate()))
			.startDate(data.getStartDate())
			.createdAt(data.getCreatedAt().orElseThrow())
			.build();
	}

	private FinancialProduct savings(FinancialProductData data) {
		return SavingsProduct.builder()
			.id(data.getProductId().orElseThrow())
			.userId(data.getUserId().orElseThrow())
			.name(data.getName())
			.productInvestmentType(ProductInvestmentType.from(data.getInvestmentType()))
			.amount(ProductAmount.from(Money.of(data.getAmount(), data.getCurrencyCode())))
			.months(new ProductMonths(data.getMonths()))
			.paymentDay(new PaymentDay(data.getPaymentDay().orElse(null)))
			.productAnnualInterestRate(new ProductAnnualInterestRate(data.getInterestRate()))
			.productInterestType(ProductInterestType.from(data.getInterestType()))
			.productTaxType(ProductTaxType.from(data.getTaxType()))
			.productTaxRate(new ProductTaxRate(data.getTaxRate()))
			.startDate(data.getStartDate())
			.createdAt(data.getCreatedAt().orElseThrow())
			.build();
	}
}
