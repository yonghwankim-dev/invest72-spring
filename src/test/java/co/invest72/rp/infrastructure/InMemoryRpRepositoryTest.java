package co.invest72.rp.infrastructure;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductTaxRate;
import co.invest72.financial_product.domain.ProductTaxType;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Currency;
import co.invest72.rp.entity.RepurchaseAgreementEntity;

class InMemoryRpRepositoryTest {

	@Test
	@DisplayName("RP 데이터 조회")
	void should_return_rp_given_id() {
		// given
		RpRepository repository = new InMemoryRpRepository();
		LocalDate startDate = LocalDate.of(2026, 1, 1);
		RepurchaseAgreementEntity entity = RepurchaseAgreementEntity.builder()
			.id(UUID.randomUUID().toString())
			.userId(UUID.randomUUID().toString())
			.name("미래에셋증권 RP")
			.amount(ProductAmount.of(BigDecimal.valueOf(1_000_000), Currency.won().getCode()))
			.days(30)
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.034)))
			.productInterestType(ProductInterestType.from(InterestType.COMPOUND))
			.productTaxType(ProductTaxType.from(TaxType.STANDARD))
			.productTaxRate(new ProductTaxRate(BigDecimal.valueOf(0.154)))
			.startDate(startDate)
			.createdAt(startDate.atStartOfDay())
			.build();
		repository.save(entity);
		String id = entity.getId();
		// when
		Optional<RepurchaseAgreementEntity> findEntity = repository.findById(id);
		// then
		Assertions.assertThat(findEntity).contains(entity);
	}

	@Test
	@DisplayName("RP 데이터 저장")
	void should_save_rp() {
		// given
		RpRepository repository = new InMemoryRpRepository();
		LocalDate startDate = LocalDate.of(2026, 1, 1);
		RepurchaseAgreementEntity entity = RepurchaseAgreementEntity.builder()
			.id(UUID.randomUUID().toString())
			.userId(UUID.randomUUID().toString())
			.name("미래에셋증권 RP")
			.amount(ProductAmount.of(BigDecimal.valueOf(1_000_000), Currency.won().getCode()))
			.days(30)
			.productAnnualInterestRate(new ProductAnnualInterestRate(BigDecimal.valueOf(0.034)))
			.productInterestType(ProductInterestType.from(InterestType.COMPOUND))
			.productTaxType(ProductTaxType.from(TaxType.STANDARD))
			.productTaxRate(new ProductTaxRate(BigDecimal.valueOf(0.154)))
			.startDate(startDate)
			.createdAt(startDate.atStartOfDay())
			.build();
		// when
		repository.save(entity);
		// then
		Assertions.assertThat(repository.findById(entity.getId())).contains(entity);
	}
}
