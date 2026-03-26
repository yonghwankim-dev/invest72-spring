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
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.entity.FinancialProductData;
import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequest;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Currency;
import source.FinancialProductDataProvider;

class FinancialProductFactoryTest {

	private FinancialProductFactory factory;

	@BeforeEach
	void setUp() {
		LocalDateProvider localDateProvider = Mockito.mock(LocalDateProvider.class);
		BDDMockito.given(localDateProvider.nowDateTime())
			.willReturn(LocalDate.of(2026, 1, 1).atStartOfDay());
		ProductIdGenerator idGenerator = Mockito.mock(ProductIdGenerator.class);
		BDDMockito.given(idGenerator.generateId())
			.willReturn("product-1234");
		factory = new FinancialProductFactory(localDateProvider, idGenerator);
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
			.build();
		String userId = "user-1234";
		dto = dto
			.withUserId(userId);
		// when
		FinancialProduct product = factory.create(dto);
		// then
		FinancialProduct expected = FinancialProductDataProvider.createCashProduct(userId);
		Assertions.assertThat(product).isEqualTo(expected);
	}
}
