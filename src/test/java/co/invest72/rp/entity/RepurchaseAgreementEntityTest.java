package co.invest72.rp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductInvestmentType;
import co.invest72.financial_product.domain.ProductTaxRate;
import co.invest72.financial_product.domain.ProductTaxType;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;

class RepurchaseAgreementEntityTest {
	@ParameterizedTest
	@ValueSource(ints = {-1, 0})
	@DisplayName("약정 일수가 0일 이하인 경우 예외를 발생시켜야 한다.")
	void should_throw_exception_when_days_zero_or_negative(int days) {
		// given
		ExchangeRate exchangeRate = new ExchangeRate("KRW", "한국 원", BigDecimal.ONE);
		LocalDate startDate = LocalDate.of(2026, 1, 1);
		// when & then
		Assertions.assertThatThrownBy(() -> {
				RepurchaseAgreementEntity.builder()
					.id(UUID.randomUUID().toString())
					.userId(UUID.randomUUID().toString())
					.productInvestmentType(ProductInvestmentType.from(InvestmentType.RP))
					.name("미래에셋증권 RP")
					.amount(ProductAmount.of(BigDecimal.valueOf(1_000_000), exchangeRate))
					.days(days) // 약정 일수 설정
					.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.034)))
					.productInterestType(ProductInterestType.from(InterestType.COMPOUND))
					.productTaxType(ProductTaxType.from(TaxType.STANDARD))
					.productTaxRate(new ProductTaxRate(BigDecimal.valueOf(0.154)))
					.startDate(startDate)
					.createdAt(startDate.atStartOfDay())
					.build();
			}).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("days must not zero or not negative, days=" + days);
	}
}
