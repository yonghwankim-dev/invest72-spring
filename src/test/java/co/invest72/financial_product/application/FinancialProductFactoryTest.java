package co.invest72.financial_product.application;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.CashProduct;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.entity.FinancialProductData;
import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequest;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;
import source.FinancialProductDataProvider;

class FinancialProductFactoryTest {

	private FinancialProductFactory factory;
	private ProductIdGenerator idGenerator;
	private String userId;

	@BeforeEach
	void setUp() {
		LocalDateProvider localDateProvider = Mockito.mock(LocalDateProvider.class);
		BDDMockito.given(localDateProvider.nowDateTime())
			.willReturn(LocalDate.of(2026, 1, 1).atStartOfDay());
		idGenerator = Mockito.mock(ProductIdGenerator.class);
		BDDMockito.given(idGenerator.generateId())
			.willReturn("product-1234");
		factory = new FinancialProductFactory(localDateProvider, idGenerator);
		userId = "user-1234";
	}

	@DisplayName("현금 상품 생성")
	@Test
	void givenDto_whenInvestmentTypeIsCash_thenReturnCashProduct() {
		// given
		FinancialProductData dto = FinancialProductRequest.builder()
			.name("현금 상품")
			.investmentType(InvestmentType.CASH.name())
			.amount(BigDecimal.valueOf(1_000_000))
			.months(0)
			.paymentDay(null)
			.interestRate(BigDecimal.ZERO)
			.interestType(InterestType.NONE.name())
			.taxType(TaxType.NONE.name())
			.taxRate(BigDecimal.ZERO)
			.startDate(LocalDate.of(2026, 1, 1))
			.currencyCode(Currency.won().getCode())
			.userId(userId)
			.build();
		// when
		FinancialProduct product = factory.create(dto);
		// then
		FinancialProduct expected = FinancialProductDataProvider.createCashProduct(userId);
		Assertions.assertThat(product).isEqualTo(expected);
	}

	@DisplayName("예금 상품 생성")
	@Test
	void givenDto_whenInvestmentTypeIsDeposit_thenReturnProduct() {
		// given
		BDDMockito.given(idGenerator.generateId())
			.willReturn("product-4567");
		FinancialProductData dto = FinancialProductRequest.builder()
			.name("예금 상품")
			.investmentType(InvestmentType.DEPOSIT.name())
			.amount(BigDecimal.valueOf(1_000_000))
			.months(12)
			.paymentDay(null)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.SIMPLE.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
			.startDate(LocalDate.of(2026, 1, 1))
			.currencyCode(Currency.won().getCode())
			.userId(userId)
			.build();
		// when
		FinancialProduct product = factory.create(dto);
		// then
		FinancialProduct expected = FinancialProductDataProvider.createDepositProduct(userId);
		Assertions.assertThat(product).isEqualTo(expected);
	}

	@DisplayName("적금 상품 생성")
	@Test
	void givenDto_whenInvestmentTypeIsSavings_thenReturnProduct() {
		// given
		BDDMockito.given(idGenerator.generateId())
			.willReturn("product-1356");
		FinancialProductData dto = FinancialProductRequest.builder()
			.name("적금 상품")
			.investmentType(InvestmentType.SAVINGS.name())
			.amount(BigDecimal.valueOf(1_000_000))
			.months(12)
			.paymentDay(15)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.SIMPLE.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
			.startDate(LocalDate.of(2026, 1, 1))
			.currencyCode(Currency.won().getCode())
			.userId(userId)
			.build();
		// when
		FinancialProduct product = factory.create(dto);
		// then
		FinancialProduct expected = FinancialProductDataProvider.createSavingsProduct(userId);
		Assertions.assertThat(product).isEqualTo(expected);
	}

	@DisplayName("현금 상품 수정")
	@Test
	void givenDto_whenInvestmentTypeIsCash_thenReturnUpdatedProduct() {
		// given
		FinancialProduct originCash = FinancialProductDataProvider.createCashProduct(userId);
		FinancialProductData dto = FinancialProductRequest.builder()
			.name("현금 상품")
			.investmentType(InvestmentType.CASH.name())
			.amount(BigDecimal.valueOf(2_000_000)) // 값 변경
			.months(0)
			.paymentDay(null)
			.interestRate(BigDecimal.ZERO)
			.interestType(InterestType.NONE.name())
			.taxType(TaxType.NONE.name())
			.taxRate(BigDecimal.ZERO)
			.startDate(LocalDate.of(2026, 1, 1))
			.currencyCode(Currency.won().getCode())
			.productId(originCash.getId())
			.userId(userId)
			.createdAt(originCash.getCreatedAt())
			.build();
		// when
		FinancialProduct product = factory.createUpdatedProduct(originCash, dto);
		// then
		FinancialProduct expected = ((CashProduct)FinancialProductDataProvider.createCashProduct(userId)).toBuilder()
			.amount(ProductAmount.from(Money.won(BigDecimal.valueOf(2_000_000))))
			.build();
		Assertions.assertThat(product).isEqualTo(expected);
	}
}
