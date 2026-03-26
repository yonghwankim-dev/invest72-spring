package co.invest72.financial_product.application;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.common.time.SystemDateTimeProvider;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.entity.FinancialProductData;
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequest;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Currency;

class FinancialProductFactoryTest {

	private FinancialProductFactory factory;

	@BeforeEach
	void setUp() {
		LocalDateProvider localDateProvider = new SystemDateTimeProvider();
		factory = new FinancialProductFactory(localDateProvider);
	}

	@DisplayName("현금 상품 생성")
	@Test
	void givenDto_whenInvestmentTypeIsCash_thenReturnCashProduct() {
		// given
		FinancialProductData dto = FinancialProductRequest.builder()
			.name("하나은행 현금")
			.investmentType(InvestmentType.CASH.name())
			.amount(BigDecimal.valueOf(1_000_000))
			.months(12)
			.paymentDay(null)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.NONE.name())
			.taxType(TaxType.NONE.name())
			.taxRate(BigDecimal.ZERO)
			.startDate(LocalDate.of(2026, 1, 1))
			.currencyCode(Currency.won().getCode())
			.build();
		// when
		FinancialProduct product = factory.create("user-1234", dto);
		// then
		Assertions.assertThat(product).isNotNull();
	}
}
