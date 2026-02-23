package co.invest72.financial_product.presentation;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.financial_product.domain.ProductType;
import co.invest72.financial_product.presentation.dto.request.CreateFinancialProductDto;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.security.PrincipalUser;
import co.invest72.user.domain.User;

@SpringBootTest
@AutoConfigureMockMvc
class FinancialProductRestControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private IdGenerator idGenerator;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private FinancialProductRepository financialProductRepository;

	private PrincipalUser principalUser;

	@BeforeEach
	void setUp() {
		String id = idGenerator.generateId();
		String email = "user1@gmail.com";
		String nickname = "user1";
		String providerId = idGenerator.generateId();
		User testUser = new User(id, email, nickname, providerId);
		principalUser = PrincipalUser.of()
			.user(testUser)
			.build();
	}

	@AfterEach
	void tearDown() {
		financialProductRepository.clear();
	}

	@Test
	@DisplayName("상품 생성 - 현금 상품을 성공적으로 생성한다")
	void createProduct_whenProductTypeIsCash_thenSaveProduct() throws Exception {
		// given
		CreateFinancialProductDto dto = CreateFinancialProductDto.builder()
			.name("현금 상품")
			.productType(ProductType.CASH.name())
			.amount(BigDecimal.valueOf(1_000_000L))
			.months(0)
			.interestRate(BigDecimal.valueOf(0.0))
			.interestType(InterestType.NONE.name())
			.taxType(TaxType.NONE.name())
			.taxRate(BigDecimal.valueOf(0.0))
			.startDate(LocalDate.of(2026, 1, 1))
			.build();
		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(notNullValue()));
	}

	@DisplayName("상품 생성 - null 데이터를 가진 현금 상품 생성 요청은 400 Bad Request를 반환한다")
	@Test
	void createProduct_whenDataIsNull_thenReturnBadRequest() throws Exception {
		CreateFinancialProductDto dto = CreateFinancialProductDto.builder().build();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors", hasSize(9)))
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("상품 생성 - 범위 값을 벗어난 데이터를 가진 현금 상품 생성 요청은 400 Bad Request를 반환한다")
	@Test
	void createProduct_whenDataIsOutOfRange_thenReturnBadRequest() throws Exception {
		CreateFinancialProductDto dto = CreateFinancialProductDto.builder()
			.name("현금 상품")
			.productType(ProductType.CASH.name())
			.amount(BigDecimal.valueOf(-1)) // 음수 금액
			.months(-1) // 음수 기간
			.interestRate(BigDecimal.valueOf(-0.01)) // 음수 이자율
			.interestType(InterestType.NONE.name())
			.taxType(TaxType.NONE.name())
			.taxRate(BigDecimal.valueOf(-0.01)) // 음수 세율
			.startDate(LocalDate.of(2026, 1, 1))
			.build();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors", hasSize(4)))
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("상품 생성 - 유효하지 않은 enum 값을 가진 현금 상품 생성 요청은 400 Bad Request를 반환한다")
	@Test
	void createProduct_whenEnumValueIsInvalid_thenReturnBadRequest() throws Exception {
		CreateFinancialProductDto dto = CreateFinancialProductDto.builder()
			.name("현금 상품")
			.productType("INVALID_TYPE") // 유효하지 않은 상품 유형
			.amount(BigDecimal.valueOf(1_000_000L))
			.months(0)
			.interestRate(BigDecimal.valueOf(0.0))
			.interestType("INVALID_INTEREST_TYPE") // 유효하지 않은 이자 유형
			.taxType("INVALID_TAX_TYPE") // 유효하지 않은 세금 유형
			.taxRate(BigDecimal.valueOf(0.0))
			.startDate(LocalDate.of(2026, 1, 1))
			.build();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors", hasSize(3)))
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("상품 목록 조회 - 사용자가 생성한 상품 목록을 조회한다")
	@Test
	void getProducts_whenUserHasProducts_thenReturnProductList() throws Exception {
		// given
		FinancialProduct product = FinancialProduct.builder()
			.user(principalUser.getUser())
			.name("현금 상품")
			.productType(ProductType.CASH)
			.amount(BigDecimal.valueOf(1_000_000L))
			.months(0)
			.interestRate(BigDecimal.valueOf(0.0))
			.interestType(InterestType.NONE)
			.taxType(TaxType.NONE)
			.taxRate(BigDecimal.valueOf(0.0))
			.startDate(LocalDate.of(2026, 1, 1))
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
		financialProductRepository.save(product);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$[0].id").value(notNullValue()))
			.andExpect(jsonPath("$[0].userId").value(principalUser.getUser().getId()))
			.andExpect(jsonPath("$[0].name").value("현금 상품"))
			.andExpect(jsonPath("$[0].productType").value(ProductType.CASH.name()))
			.andExpect(jsonPath("$[0].amount").value(1_000_000.0))
			.andExpect(jsonPath("$[0].months").value(0))
			.andExpect(jsonPath("$[0].interestRate").value(0.0))
			.andExpect(jsonPath("$[0].interestType").value(InterestType.NONE.name()))
			.andExpect(jsonPath("$[0].taxType").value(TaxType.NONE.name()))
			.andExpect(jsonPath("$[0].taxRate").value(0.0))
			.andExpect(jsonPath("$[0].startDate").value("2026-01-01"))
			.andExpect(jsonPath("$[0].createdAt").value(notNullValue()));
	}
}
