package co.invest72.rp.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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

	@Nested
	@DisplayName("RP 엔티티 저장 단위 테스트")
	class createRpTests {

		private RpService service;
		private String rpId;
		private User user;

		@BeforeEach
		void setUp() {
			String userId = UUID.randomUUID().toString();
			user = new User("user1@gmail.com", "user1", userId);

			IdGenerator idGenerator = BDDMockito.mock(IdGenerator.class);
			rpId = UUID.randomUUID().toString();
			BDDMockito.given(idGenerator.generateId())
				.willReturn(rpId);
			LocalDateProvider localDateProvider = BDDMockito.mock(LocalDateProvider.class);
			LocalDate startDate = LocalDate.of(2026, 1, 1);
			BDDMockito.given(localDateProvider.nowDateTime())
				.willReturn(startDate.atStartOfDay());
			RpRepository repository = new InMemoryRpRepository();
			service = new RpService(idGenerator, localDateProvider, repository);
		}

		@Test
		@DisplayName("RP 엔티티를 저장소에 저장한다")
		void should_return_created_rp_entity_id_when_save_rp_data() {
			// given
			RpCreateRequest request = RpCreateRequest.builder()
				.name("미래에셋증권 RP")
				.investmentType(InvestmentType.RP.name())
				.amount(BigDecimal.valueOf(1_000_000))
				.days(30)
				.interestRate(BigDecimal.valueOf(0.03))
				.interestType(InterestType.COMPOUND.name())
				.taxType(TaxType.STANDARD.name())
				.taxRate(BigDecimal.valueOf(0.154))
				.startDate(LocalDate.of(2026, 1, 1))
				.currencyCode(Currency.won().getCode())
				.build();

			// when
			String id = service.createRp(user, request);

			// then
			Assertions.assertThat(id).isEqualTo(rpId);
		}

		@ParameterizedTest
		@ValueSource(ints = {-1, 0})
		@DisplayName("약정 일수가 0일 이하인 경우 예외를 발생시켜야 한다.")
		void should_throw_exception_when_days_zero_or_negative(int days) {
			// given
			RpCreateRequest request = RpCreateRequest.builder()
				.name("미래에셋증권 RP")
				.investmentType(InvestmentType.RP.name())
				.amount(BigDecimal.valueOf(1_000_000))
				.days(days) // 약정 일수 설정
				.interestRate(BigDecimal.valueOf(0.03))
				.interestType(InterestType.COMPOUND.name())
				.taxType(TaxType.STANDARD.name())
				.taxRate(BigDecimal.valueOf(0.154))
				.startDate(LocalDate.of(2026, 1, 1))
				.currencyCode(Currency.won().getCode())
				.build();

			// when
			Assertions.assertThatThrownBy(() -> service.createRp(user, request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("days must not zero or not negative, days=" + days);
		}
	}

	@Nested
	@DisplayName("RP 상품 정보 조회")
	class getRpTests {

		private static Stream<Arguments> holdingPeriodSource() {
			return Stream.of(
				Arguments.of(LocalDate.of(2026, 1, 1), BigDecimal.ZERO, BigDecimal.ZERO), // 예치 일수 0일
				Arguments.of(LocalDate.of(2026, 1, 2), BigDecimal.valueOf(93), BigDecimal.valueOf(0.0001)), // 예치 일수 1일
				Arguments.of(LocalDate.of(2026, 1, 16), BigDecimal.valueOf(1397), BigDecimal.valueOf(0.0014)),
				// 예치 일수 1일
				Arguments.of(LocalDate.of(2026, 1, 31), BigDecimal.valueOf(2795), BigDecimal.valueOf(0.0028))
				// 예치 일수 30일
			);
		}

		@ParameterizedTest(name = "예치일자={0}, 예상현재이자금액={1}, 예상현재이자수익율={2}")
		@MethodSource(value = "holdingPeriodSource")
		void should_return_rp_details_with_accrued_interest_for_given_holding_period(LocalDate now,
			BigDecimal expectedCurrentInterest, BigDecimal expectedCurrentInterestRate) {
			// given
			IdGenerator idGenerator = BDDMockito.mock(IdGenerator.class);
			String rpId = UUID.randomUUID().toString();
			LocalDateProvider localDateProvider = BDDMockito.mock(LocalDateProvider.class);
			BDDMockito.given(localDateProvider.now())
				.willReturn(now);
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
				.currentInterest(expectedCurrentInterest)
				.currentInterestRate(expectedCurrentInterestRate)
				.isAutoReinvest(true)
				.build();
			Assertions.assertThat(response)
				.usingRecursiveComparison()
				.withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
				.isEqualTo(expected);
		}

		@Test
		@DisplayName("RP 엔티티 데이터를 찾을 수 없다면 예외를 발생시켜야 한다")
		void should_throw_exception_when_not_found_rp_entity() {
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
			RpService service = new RpService(idGenerator, localDateProvider, repository);
			// when & then
			Assertions.assertThatThrownBy(() -> service.getRp(rpId))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessage("not found rp, id=" + rpId);
		}

	}
}
