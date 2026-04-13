package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.application.FinancialProductFactory;
import co.invest72.financial_product.domain.entity.FinancialProductData;
import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequest;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.exchange_rate.domain.Currency;
import co.invest72.money.domain.Money;
import source.FinancialProductDataProvider;

class FinancialProductTest {

	private String userId;
	private FinancialProductFactory factory;

	@BeforeEach
	void setUp() {
		userId = "user-1234";
		LocalDateProvider localDateProvider = Mockito.mock(LocalDateProvider.class);
		BDDMockito.given(localDateProvider.nowDateTime())
			.willReturn(LocalDate.of(2026, 1, 1).atStartOfDay());
		ProductIdGenerator idGenerator = Mockito.mock(ProductIdGenerator.class);
		BDDMockito.given(idGenerator.generateId())
			.willReturn("product-1234");
		factory = new FinancialProductFactory(localDateProvider, idGenerator);
	}

	@DisplayName("객체 생성 - 적금 상품 생성시 이체일이 초기화되지 않는 경우 예외가 발생한다.")
	@Test
	void constructor_whenCreatingSavingsProduct_thenThrowExceptionIfPaymentDayIsNotSet() {
		// Given
		SavingsProduct savings = (SavingsProduct)FinancialProductDataProvider.createSavingsProduct("user-1");

		// when
		Throwable throwable = Assertions.catchThrowable(() -> savings.toBuilder()
			.paymentDay(null)
			.build());
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("객체 생성 - 빌더를 이용하여 현금 생성")
	@Test
	void newInstance_whenInvestmentTypeIsCash_thenReturnProduct() {
		FinancialProduct product = FinancialProductDataProvider.createCashProduct("user-1234");

		Assertions.assertThat(product).isNotNull();
	}

	@DisplayName("현금 상품 수정")
	@Test
	void givenDto_whenInvestmentTypeIsCash_thenReturnUpdatedProduct() {
		// given
		FinancialProduct originProduct = FinancialProductDataProvider.createCashProduct(userId);
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
			.productId(originProduct.getId())
			.userId(originProduct.getUserId())
			.createdAt(originProduct.getCreatedAt())
			.build();
		FinancialProduct updateProduct = factory.toEntity(dto);
		// when
		originProduct.update(updateProduct);
		// then
		FinancialProduct expected = ((CashProduct)FinancialProductDataProvider.createCashProduct(userId)).toBuilder()
			.amount(ProductAmount.from(Money.won(BigDecimal.valueOf(2_000_000))))
			.build();
		Assertions.assertThat(originProduct).isEqualTo(expected);
	}

	@DisplayName("예금 상품 수정")
	@Test
	void givenDto_whenInvestmentTypeIsDeposit_thenReturnUpdatedProduct() {
		// given
		FinancialProduct originProduct = FinancialProductDataProvider.createDepositProduct(userId);
		FinancialProductData dto = FinancialProductRequest.builder()
			.name("예금 상품")
			.investmentType(InvestmentType.DEPOSIT.name())
			.amount(BigDecimal.valueOf(2_000_000)) // 값 변경
			.months(12)
			.paymentDay(null)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.SIMPLE.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
			.startDate(LocalDate.of(2026, 1, 1))
			.currencyCode(Currency.won().getCode())
			.productId(originProduct.getId())
			.userId(originProduct.getUserId())
			.createdAt(originProduct.getCreatedAt())
			.build();
		FinancialProduct updateProduct = factory.toEntity(dto);
		// when
		originProduct.update(updateProduct);
		// then
		FinancialProduct expected = ((DepositProduct)FinancialProductDataProvider.createDepositProduct(
			userId)).toBuilder()
			.amount(ProductAmount.from(Money.won(BigDecimal.valueOf(2_000_000))))
			.build();
		Assertions.assertThat(originProduct).isEqualTo(expected);
	}

	@DisplayName("적금 상품 수정")
	@Test
	void givenDto_whenInvestmentTypeIsSavings_thenReturnUpdatedProduct() {
		// given
		FinancialProduct originProduct = FinancialProductDataProvider.createSavingsProduct(userId);
		FinancialProductData dto = FinancialProductRequest.builder()
			.name("적금 상품")
			.investmentType(InvestmentType.SAVINGS.name())
			.amount(BigDecimal.valueOf(2_000_000)) // 값 변경
			.months(12)
			.paymentDay(15)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.SIMPLE.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
			.startDate(LocalDate.of(2026, 1, 1))
			.currencyCode(Currency.won().getCode())
			.productId(originProduct.getId())
			.userId(originProduct.getUserId())
			.createdAt(originProduct.getCreatedAt())
			.build();
		FinancialProduct updateProduct = factory.toEntity(dto);
		// when
		originProduct.update(updateProduct);
		// then
		FinancialProduct expected = ((SavingsProduct)FinancialProductDataProvider.createSavingsProduct(
			userId)).toBuilder()
			.amount(ProductAmount.from(Money.won(BigDecimal.valueOf(2_000_000))))
			.build();
		Assertions.assertThat(originProduct).isEqualTo(expected);
	}
}
