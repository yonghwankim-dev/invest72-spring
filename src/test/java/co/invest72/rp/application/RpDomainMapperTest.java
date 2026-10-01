package co.invest72.rp.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
import co.invest72.rp.domain.RepurchaseAgreement;
import co.invest72.rp.domain.TermRepurchaseAgreement;
import co.invest72.rp.entity.RepurchaseAgreementEntity;

class RpDomainMapperTest {

	@Nested
	@DisplayName("RP 도메인 변환")
	class toDomain {
		@Test
		void should_return_rp_domain() {
			// given
			RpDomainMapper mapper = new RpDomainMapper();
			ExchangeRate exchangeRate = new ExchangeRate("KRW", "한국 원", BigDecimal.ONE);
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			int days = 30;
			RepurchaseAgreementEntity entity = RepurchaseAgreementEntity.builder()
				.id(UUID.randomUUID().toString())
				.userId(UUID.randomUUID().toString())
				.productInvestmentType(ProductInvestmentType.from(InvestmentType.RP))
				.name("미래에셋증권 RP")
				.amount(ProductAmount.of(BigDecimal.valueOf(1_000_000), exchangeRate))
				.days(days)
				.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.034)))
				.productInterestType(ProductInterestType.from(InterestType.COMPOUND))
				.productTaxType(ProductTaxType.from(TaxType.STANDARD))
				.productTaxRate(new ProductTaxRate(BigDecimal.valueOf(0.154)))
				.startDate(startDate)
				.createdAt(startDate.atStartOfDay())
				.build();
			// when
			RepurchaseAgreement rp = mapper.toDomain(entity);
			// then
			Assertions.assertThat(rp)
				.isInstanceOf(TermRepurchaseAgreement.class)
				.isNotNull();
		}

		@Test
		@DisplayName("entity의 값이 null이면 예외를 발생시켜야 한다")
		void should_throw_exception_when_entity_is_null() {
			// given
			RpDomainMapper mapper = new RpDomainMapper();
			// when & then
			Assertions.assertThatThrownBy(() -> mapper.toDomain(null))
				.isInstanceOf(NullPointerException.class)
				.hasMessage("entity must not be null");
		}
	}

}
