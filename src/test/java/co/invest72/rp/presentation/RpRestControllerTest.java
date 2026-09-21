package co.invest72.rp.presentation;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductAnnualInterestRate;
import co.invest72.financial_product.domain.ProductInterestType;
import co.invest72.financial_product.domain.ProductTaxRate;
import co.invest72.financial_product.domain.ProductTaxType;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.money.domain.Currency;
import co.invest72.rp.entity.RepurchaseAgreementEntity;
import co.invest72.rp.infrastructure.RpRepository;
import co.invest72.rp.presentation.dto.RpCreateRequest;
import co.invest72.security.PrincipalUser;
import co.invest72.user.domain.User;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RpRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private LocalDateProvider localDateProviderMock;

	@Autowired
	private RpRepository repository;

	private PrincipalUser principalUser;

	@BeforeEach
	void setUp() {
		String email = "user1@gmail.com";
		String nickname = "user1";
		String providerId = UUID.randomUUID().toString();
		User testUser = new User(email, nickname, providerId);
		principalUser = PrincipalUser.of()
			.user(testUser)
			.authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
			.build();

		LocalDateTime createdAt = LocalDate.of(2026, 1, 1).atStartOfDay();
		BDDMockito.given(localDateProviderMock.nowDateTime()).willReturn(createdAt);
	}

	@DisplayName("상품 생성 - RP 상품")
	@Test
	void createProduct_whenInvestmentTypeIsRP_thenSaveProduct() throws Exception {
		// given
		RpCreateRequest request = RpCreateRequest.builder()
			.name("미래에셋증권 RP")
			.investmentType(InvestmentType.RP.name())
			.amount(BigDecimal.valueOf(1_000_000L))
			.days(30)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.COMPOUND.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
			.startDate(LocalDate.of(2026, 1, 1))
			.currencyCode(Currency.won().getCode())
			.build();
		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rp")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(notNullValue()));
	}

	@DisplayName("RP 상품 조회")
	@Test
	void should_return_rp_data() throws Exception {
		// given
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
		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rp")
				.queryParam("id", entity.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(entity.getId()));
	}

}
