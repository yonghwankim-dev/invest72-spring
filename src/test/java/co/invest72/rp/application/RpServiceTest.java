package co.invest72.rp.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductInvestmentType;
import co.invest72.financial_product.domain.ProductTaxRate;
import co.invest72.financial_product.domain.ProductTaxType;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Currency;
import co.invest72.rp.entity.RepurchaseAgreementEntity;
import co.invest72.rp.infrastructure.InMemoryRpRepository;
import co.invest72.rp.infrastructure.RpRepository;
import co.invest72.rp.presentation.dto.RpCreateRequest;
import co.invest72.rp.presentation.dto.RpDetailedResponse;
import co.invest72.user.domain.User;

class RpServiceTest {

	@Test
	@DisplayName("서비스 객체 생성")
	void can_create_instance() {
		IdGenerator idGenerator = BDDMockito.mock(IdGenerator.class);
		LocalDateProvider localDateProvider = BDDMockito.mock(LocalDateProvider.class);
		RpRepository repository = new InMemoryRpRepository();

		RpService service = new RpService(idGenerator, localDateProvider, repository);

		Assertions.assertThat(service).isNotNull();
	}

	@Nested
	@DisplayName("RP 엔티티 저장")
	class createRp {
		@Test
		@DisplayName("RP 엔티티를 저장소에 저장한다")
		void save_rp_entity() {
			// given
			IdGenerator idGenerator = BDDMockito.mock(IdGenerator.class);
			String rpId = UUID.randomUUID().toString();
			BDDMockito.given(idGenerator.generateId())
				.willReturn(rpId);
			LocalDateProvider localDateProvider = BDDMockito.mock(LocalDateProvider.class);
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			BDDMockito.given(localDateProvider.nowDateTime())
				.willReturn(startDate.atStartOfDay());
			RpRepository repository = new InMemoryRpRepository();
			RpService service = new RpService(idGenerator, localDateProvider, repository);
			User user = new User("user1@gmail.com", "user1", UUID.randomUUID().toString());
			RpCreateRequest request = RpCreateRequest.builder()
				.name("미래에셋증권 RP")
				.investmentType(InvestmentType.RP.name())
				.amount(BigDecimal.valueOf(1_000_000))
				.days(30)
				.interestRate(BigDecimal.valueOf(0.03))
				.interestType(InterestType.COMPOUND.name())
				.taxType(TaxType.STANDARD.name())
				.taxRate(BigDecimal.valueOf(0.154))
				.startDate(startDate)
				.currencyCode(Currency.won().getCode())
				.build();

			// when
			String id = service.createRp(user, request);

			// then
			Assertions.assertThat(id).isEqualTo(rpId);
		}
	}

	@Nested
	@DisplayName("RP 상품 정보 조회")
	class getRp {
		@Test
		@DisplayName("만기 일자에서 RP 상품의 정보 조회")
		void should_return_rp_data_when_today_is_expiration_date() {
			// given
			IdGenerator idGenerator = BDDMockito.mock(IdGenerator.class);
			String rpId = UUID.randomUUID().toString();
			LocalDateProvider localDateProvider = BDDMockito.mock(LocalDateProvider.class);
			BDDMockito.given(localDateProvider.now())
				.willReturn(LocalDate.of(2026, 1, 31));
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			BDDMockito.given(localDateProvider.nowDateTime())
				.willReturn(startDate.atStartOfDay());
			RpRepository repository = new InMemoryRpRepository();
			RepurchaseAgreementEntity entity = RepurchaseAgreementEntity.builder()
				.id(rpId)
				.userId(UUID.randomUUID().toString())
				.productInvestmentType(ProductInvestmentType.from(InvestmentType.RP))
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
			RpService service = new RpService(idGenerator, localDateProvider, repository);
			// when
			RpDetailedResponse response = service.getRp(rpId);
			// then
			RpDetailedResponse expected = RpDetailedResponse.builder()
				.id(rpId)
				.investmentType("RP")
				.name("미래에셋증권 RP")
				.amount(BigDecimal.valueOf(1_000_000))
				.currency("KRW")
				.interestRate(BigDecimal.valueOf(0.034))
				.startDate(LocalDate.of(2026, 1, 1))
				.termOfAgreement(30)
				.maturityInterest(BigDecimal.valueOf(2795))
				.currentInterest(BigDecimal.valueOf(2795))
				.currentInterestRate(BigDecimal.valueOf(0.00279))
				.isAutoReinvest(true)
				.build();
			Assertions.assertThat(response)
				.usingRecursiveComparison()
				.withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
				.isEqualTo(expected);
		}
	}
}
