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
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequest;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.investment.PaymentDay;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Currency;
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
		InvestmentType investmentType = InvestmentType.valueOf(withedData.getInvestmentType());

		return switch (investmentType) {
			case CASH -> new CashProduct(withedData);
			case DEPOSIT -> new DepositProduct(withedData);
			case SAVINGS -> new SavingsProduct(withedData);
		};
	}

	public FinancialProduct createUpdatedProduct(FinancialProduct base, FinancialProductRequest dto) {
		InvestmentType investmentType = InvestmentType.valueOf(base.getProductInvestmentType().getName());
		validateInvestmentType(base, dto);
		return switch (investmentType) {
			case CASH -> createCashProduct(base.getId(), base.getUserId(), base.getCreatedAt(), dto);
			case DEPOSIT -> createDepositProduct(base.getId(), base.getUserId(), base.getCreatedAt(), dto);
			case SAVINGS -> createSavingsProduct(base.getId(), base.getUserId(), base.getCreatedAt(), dto);
		};
	}

	private void validateInvestmentType(FinancialProduct base, FinancialProductRequest dto) {
		InvestmentType newInvestmentType = InvestmentType.valueOf(dto.getInvestmentType());
		if (InvestmentType.valueOf(base.getProductInvestmentType().getName()) != newInvestmentType) {
			throw new IllegalArgumentException("상품 유형은 변경할 수 없습니다.");
		}
	}

	private FinancialProduct createCashProduct(String productId, String userId, LocalDateTime createdAt,
		FinancialProductRequest dto) {
		Currency currency = Currency.from(dto.getCurrencyCode());
		Money amount = Money.of(dto.getAmount(), currency);
		return CashProduct.builder()
			.id(productId)
			.userId(userId)
			.name(dto.getName())
			.productInvestmentType(ProductInvestmentType.from(dto.getInvestmentType()))
			.amount(ProductAmount.from(amount))
			.months(new ProductMonths(dto.getMonths()))
			.productAnnualInterestRate(new ProductAnnualInterestRate(dto.getInterestRate()))
			.productInterestType(ProductInterestType.from(dto.getInterestType()))
			.productTaxType(ProductTaxType.from(dto.getTaxType()))
			.productTaxRate(new ProductTaxRate(dto.getTaxRate()))
			.startDate(dto.getStartDate())
			.createdAt(createdAt)
			.build();
	}

	private FinancialProduct createDepositProduct(String productId, String userId, LocalDateTime createdAt,
		FinancialProductRequest dto) {
		Currency currency = Currency.from(dto.getCurrencyCode());
		Money amount = Money.of(dto.getAmount(), currency);
		return DepositProduct.builder()
			.id(productId)
			.userId(userId)
			.name(dto.getName())
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.valueOf(dto.getInvestmentType()).name()))
			.amount(ProductAmount.from(amount))
			.months(new ProductMonths(dto.getMonths()))
			.productAnnualInterestRate(new ProductAnnualInterestRate(dto.getInterestRate()))
			.productInterestType(ProductInterestType.from(InterestType.valueOf(dto.getInterestType())))
			.productTaxType(ProductTaxType.from(TaxType.valueOf(dto.getTaxType())))
			.productTaxRate(new ProductTaxRate(dto.getTaxRate()))
			.startDate(dto.getStartDate())
			.createdAt(createdAt)
			.build();
	}

	private FinancialProduct createSavingsProduct(String productId, String userId, LocalDateTime createdAt,
		FinancialProductRequest dto) {
		Currency currency = Currency.from(dto.getCurrencyCode());
		Money amount = Money.of(dto.getAmount(), currency);
		return SavingsProduct.builder()
			.id(productId)
			.userId(userId)
			.name(dto.getName())
			.productInvestmentType(ProductInvestmentType.from(InvestmentType.valueOf(dto.getInvestmentType()).name()))
			.amount(ProductAmount.from(amount))
			.months(new ProductMonths(dto.getMonths()))
			.paymentDay(new PaymentDay(dto.getPaymentDay().orElseThrow()))
			.productAnnualInterestRate(new ProductAnnualInterestRate(dto.getInterestRate()))
			.productInterestType(ProductInterestType.from(InterestType.valueOf(dto.getInterestType())))
			.productTaxType(ProductTaxType.from(TaxType.valueOf(dto.getTaxType())))
			.productTaxRate(new ProductTaxRate(dto.getTaxRate()))
			.startDate(dto.getStartDate())
			.createdAt(createdAt)
			.build();
	}
}
