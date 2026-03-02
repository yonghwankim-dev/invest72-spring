package co.invest72.financial_product.presentation;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.invest72.common.time.LocalDateProvider;
import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.financial_product.presentation.dto.request.FinancialProductRequestDto;
import co.invest72.financial_product.presentation.dto.response.FinancialProductSummaryResponse;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.security.PrincipalUser;
import co.invest72.user.domain.User;
import co.invest72.user.infrastructure.UserIdGenerator;
import source.FinancialProductDataProvider;

@SpringBootTest
@AutoConfigureMockMvc
class FinancialProductRestControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private FinancialProductRepository financialProductRepository;

	@MockitoSpyBean
	private LocalDateProvider spyLocalDateProvider;

	private PrincipalUser principalUser;
	private IdGenerator userIdGenerator;
	private IdGenerator productIdGenerator;

	@BeforeEach
	void setUp() {
		userIdGenerator = new UserIdGenerator("user");
		String email = "user1@gmail.com";
		String nickname = "user1";
		String providerId = UUID.randomUUID().toString();
		User testUser = new User(email, nickname, providerId);
		principalUser = PrincipalUser.of()
			.user(testUser)
			.build();

		productIdGenerator = new ProductIdGenerator("product");

		BDDMockito.given(spyLocalDateProvider.now()).willReturn(LocalDate.of(2026, 2, 27));
	}

	@AfterEach
	void tearDown() {
		financialProductRepository.clear();
	}

	@Test
	@DisplayName("상품 생성 - 현금 상품을 성공적으로 생성한다")
	void createProduct_whenInvestmentTypeIsCash_thenSaveProduct() throws Exception {
		// given
		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("현금 상품")
			.investmentType(InvestmentType.CASH.name())
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
		FinancialProductRequestDto dto = FinancialProductRequestDto.builder().build();

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
		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("현금 상품")
			.investmentType(InvestmentType.CASH.name())
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
		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("현금 상품")
			.investmentType("INVALID_TYPE") // 유효하지 않은 상품 유형
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

	@DisplayName("상품 생성 - 단리-예금 상품")
	@Test
	void createProduct_whenInvestmentTypeIsDeposit_thenSaveProduct() throws Exception {
		// given
		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("예금 상품")
			.investmentType(InvestmentType.DEPOSIT.name())
			.amount(BigDecimal.valueOf(1_000_000L))
			.months(12)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.SIMPLE.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
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

	@DisplayName("상품 생성 - 복리-적금 상품")
	@Test
	void createProduct_whenInvestmentTypeIsSavings_thenSaveProduct() throws Exception {
		// given
		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("적금 상품")
			.investmentType(InvestmentType.SAVINGS.name())
			.amount(BigDecimal.valueOf(1_000_000L))
			.months(12)
			.paymentDay(15)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.COMPOUND.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
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

	@DisplayName("상품 목록 조회 - 사용자가 생성한 상품 목록을 조회한다")
	@Test
	void getProducts_whenUserHasProducts_thenReturnProductList() throws Exception {
		// given
		FinancialProduct product = FinancialProductDataProvider.createCashProduct(principalUser.getUser().getId());
		financialProductRepository.save(product);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$[0].id").value(notNullValue()))
			.andExpect(jsonPath("$[0].userId").value(principalUser.getUser().getId()))
			.andExpect(jsonPath("$[0].name").value("현금 상품"))
			.andExpect(jsonPath("$[0].investmentType").value(InvestmentType.CASH.name()))
			.andExpect(jsonPath("$[0].amount").value(1_000_000.0))
			.andExpect(jsonPath("$[0].months").value(0))
			.andExpect(jsonPath("$[0].interestRate").value(0.0))
			.andExpect(jsonPath("$[0].interestType").value(InterestType.NONE.name()))
			.andExpect(jsonPath("$[0].taxType").value(TaxType.NONE.name()))
			.andExpect(jsonPath("$[0].taxRate").value(0.0))
			.andExpect(jsonPath("$[0].startDate").value("2026-01-01"))
			.andExpect(jsonPath("$[0].createdAt").value(notNullValue()));
	}

	@DisplayName("상품 상세 조회 - 사용자가 생성한 현금 상품의 상세 정보를 조회한다")
	@Test
	void getProductDetail_whenProductIsCash_thenReturnProductDetail() throws Exception {
		// given
		FinancialProduct product = FinancialProductDataProvider.createCashProduct(principalUser.getUser().getId());
		financialProductRepository.save(product);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(product.getId()))
			.andExpect(jsonPath("$.userId").value(principalUser.getUser().getId()))
			.andExpect(jsonPath("$.name").value("현금 상품"))
			.andExpect(jsonPath("$.investmentType").value(InvestmentType.CASH.name()))
			.andExpect(jsonPath("$.amount").value(1_000_000.0))
			.andExpect(jsonPath("$.months").value(0))
			.andExpect(jsonPath("$.interestRate").value(0.0))
			.andExpect(jsonPath("$.interestType").value(InterestType.NONE.name()))
			.andExpect(jsonPath("$.taxType").value(TaxType.NONE.name()))
			.andExpect(jsonPath("$.taxRate").value(0.0))
			.andExpect(jsonPath("$.startDate").value("2026-01-01"))
			.andExpect(jsonPath("$.createdAt").value(notNullValue()))
			.andExpect(jsonPath("$.expirationDate").value("+999999999-12-31"))
			.andExpect(jsonPath("$.balance").value(1_000_000.0))
			.andExpect(jsonPath("$.progress").value(1.0))
			.andExpect(jsonPath("$.remainingDays").value(0));
	}

	@DisplayName("상품 상세 조회 - 사용자가 생성한 예금 상품의 상세 정보를 조회한다")
	@Test
	void getProductDetail_whenProductIsDeposit_thenReturnProductDetail() throws Exception {
		// given
		FinancialProduct product = FinancialProductDataProvider.createDepositProduct(principalUser.getUser().getId());
		financialProductRepository.save(product);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(product.getId()))
			.andExpect(jsonPath("$.userId").value(principalUser.getUser().getId()))
			.andExpect(jsonPath("$.name").value("예금 상품"))
			.andExpect(jsonPath("$.investmentType").value(InvestmentType.DEPOSIT.name()))
			.andExpect(jsonPath("$.amount").value(1_000_000.0))
			.andExpect(jsonPath("$.months").value(12))
			.andExpect(jsonPath("$.interestRate").value(0.05))
			.andExpect(jsonPath("$.interestType").value(InterestType.SIMPLE.name()))
			.andExpect(jsonPath("$.taxType").value(TaxType.STANDARD.name()))
			.andExpect(jsonPath("$.taxRate").value(0.154))
			.andExpect(jsonPath("$.startDate").value("2026-01-01"))
			.andExpect(jsonPath("$.createdAt").value(notNullValue()))
			.andExpect(jsonPath("$.expirationDate").value("2027-01-01"))
			.andExpect(jsonPath("$.balance").value(1_000_000.0))
			.andExpect(jsonPath("$.progress").value(0.16))
			.andExpect(jsonPath("$.remainingDays").value(308));
	}

	@DisplayName("상품 상세 조회 - 사용자가 생성한 적금 상품의 상세 정보를 조회한다")
	@Test
	void getProductDetail_whenProductIsSavings_thenReturnProductDetail() throws Exception {
		// given
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct(principalUser.getUser().getId(),
			InterestType.COMPOUND);
		financialProductRepository.save(product);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(product.getId()))
			.andExpect(jsonPath("$.userId").value(principalUser.getUser().getId()))
			.andExpect(jsonPath("$.name").value("적금 상품"))
			.andExpect(jsonPath("$.investmentType").value(InvestmentType.SAVINGS.name()))
			.andExpect(jsonPath("$.amount").value(1_000_000.0))
			.andExpect(jsonPath("$.months").value(12))
			.andExpect(jsonPath("$.interestRate").value(0.05))
			.andExpect(jsonPath("$.interestType").value(InterestType.COMPOUND.name()))
			.andExpect(jsonPath("$.taxType").value(TaxType.STANDARD.name()))
			.andExpect(jsonPath("$.taxRate").value(0.154))
			.andExpect(jsonPath("$.startDate").value("2026-01-01"))
			.andExpect(jsonPath("$.createdAt").value(notNullValue()))
			.andExpect(jsonPath("$.expirationDate").value("2027-01-01"))
			.andExpect(jsonPath("$.balance").value(2_000_000.0))
			.andExpect(jsonPath("$.progress").value(0.16))
			.andExpect(jsonPath("$.remainingDays").value(308))
			.andExpect(jsonPath("$.paymentDay").value(15));
	}

	@DisplayName("상품 상세 조회 - 다른 사용자가 생성한 상품의 상세 정보를 조회하려고 하면 400 Bad Request를 반환한다")
	@Test
	void getProductDetail_whenProductBelongsToAnotherUser_thenReturnBadRequest() throws Exception {
		// given
		String otherUserId = userIdGenerator.generateId();
		FinancialProduct product = FinancialProductDataProvider.createCashProduct(otherUserId);
		financialProductRepository.save(product);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Invalid request"));
	}

	@DisplayName("상품 상세 조회 - 존재하지 않는 상품의 상세 정보를 조회하려고 하면 400 Bad Request를 반환한다")
	@Test
	void getProductDetail_whenProductDoesNotExist_thenReturnBadRequest() throws Exception {
		// given
		String nonExistentProductId = productIdGenerator.generateId();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", nonExistentProductId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Invalid request"));
	}

	@DisplayName("요약 상품 목록 조회 - 사용자가 생성한 상품의 요약 정보를 조회한다")
	@Test
	void getSummaryProducts_whenUserHasProducts_thenReturnSummaryProductList() throws Exception {
		// given
		FinancialProduct product = FinancialProductDataProvider.createCashProduct(principalUser.getUser().getId());
		FinancialProduct depositProduct = FinancialProductDataProvider.createDepositProduct(
			principalUser.getUser().getId(), InterestType.SIMPLE);
		FinancialProduct savingsProduct = FinancialProductDataProvider.createSavingsProduct(
			principalUser.getUser().getId(), InterestType.COMPOUND);
		financialProductRepository.save(product);
		financialProductRepository.save(depositProduct);
		financialProductRepository.save(savingsProduct);

		FinancialProductSummaryResponse expectedResponse1 = FinancialProductSummaryResponse.builder()
			.id(savingsProduct.getId())
			.name("적금 상품")
			.investmentType(InvestmentType.SAVINGS.name())
			.interestRate(BigDecimal.valueOf(0.05))
			.startDate(LocalDate.of(2026, 1, 1))
			.expirationDate(LocalDate.of(2027, 1, 1))
			.balance(BigDecimal.valueOf(2_000_000L))
			.expectedInterest(BigDecimal.valueOf(330_017L))
			.progress(BigDecimal.valueOf(0.16))
			.remainingDays(308)
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
		FinancialProductSummaryResponse expectedResponse2 = FinancialProductSummaryResponse.builder()
			.id(depositProduct.getId())
			.name("예금 상품")
			.investmentType(InvestmentType.DEPOSIT.name())
			.interestRate(BigDecimal.valueOf(0.05))
			.startDate(LocalDate.of(2026, 1, 1))
			.expirationDate(LocalDate.of(2027, 1, 1))
			.balance(BigDecimal.valueOf(1_000_000L))
			.expectedInterest(BigDecimal.valueOf(50_000L))
			.progress(BigDecimal.valueOf(0.16))
			.remainingDays(308)
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
		FinancialProductSummaryResponse expectedResponse3 = FinancialProductSummaryResponse.builder()
			.id(product.getId())
			.name("현금 상품")
			.investmentType(InvestmentType.CASH.name())
			.interestRate(BigDecimal.ZERO)
			.startDate(LocalDate.of(2026, 1, 1))
			.expirationDate(LocalDate.MAX)
			.balance(BigDecimal.valueOf(1_000_000L))
			.expectedInterest(BigDecimal.ZERO)
			.progress(BigDecimal.ONE)
			.remainingDays(0L)
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();

		String expectedJson = objectMapper.writeValueAsString(
			Arrays.asList(expectedResponse1, expectedResponse2, expectedResponse3));
		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/summary")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(content().json(expectedJson))
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("상품 수정 - 사용자가 생성한 현금 상품을 수정한다")
	@Test
	void updateProduct_whenProductIsCash_thenUpdateProduct() throws Exception {
		// given
		FinancialProduct product = FinancialProductDataProvider.createCashProduct(principalUser.getUser().getId());
		financialProductRepository.save(product);

		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("수정된 현금 상품")
			.investmentType(InvestmentType.CASH.name())
			.amount(BigDecimal.valueOf(2_000_000L))
			.months(0)
			.interestRate(BigDecimal.valueOf(0.00))
			.interestType(InterestType.NONE.name())
			.taxType(TaxType.NONE.name())
			.taxRate(BigDecimal.valueOf(0.0))
			.startDate(LocalDate.of(2026, 2, 1))
			.build();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isNoContent());

		// 상품이 수정되었는지 검증
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(product.getId())) // ID는 변경되지 않아야 함
			.andExpect(jsonPath("$.name").value("수정된 현금 상품"))
			.andExpect(jsonPath("$.amount").value(2_000_000.0))
			.andExpect(jsonPath("$.startDate").value("2026-02-01"));
	}

	@DisplayName("상품 수정 - 사용자는 예금 상품을 수정한다")
	@Test
	void updateProduct_whenProductIsDeposit_thenUpdateProduct() throws Exception {
		// given
		FinancialProduct product = FinancialProductDataProvider.createDepositProduct(principalUser.getUser().getId(),
			InterestType.SIMPLE);
		financialProductRepository.save(product);

		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("수정된 예금 상품")
			.investmentType(InvestmentType.DEPOSIT.name())
			.amount(BigDecimal.valueOf(2_000_000L))
			.months(12)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.SIMPLE.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
			.startDate(LocalDate.of(2026, 2, 1))
			.build();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isNoContent());

		// 상품이 수정되었는지 검증
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(product.getId())) // ID는 변경되지 않아야 함
			.andExpect(jsonPath("$.name").value("수정된 예금 상품"))
			.andExpect(jsonPath("$.amount").value(2_000_000.0))
			.andExpect(jsonPath("$.startDate").value("2026-02-01"))
			.andExpect(jsonPath("$.months").value(12));
	}

	@DisplayName("상품 수정 - 사용자는 적금 상품을 수정한다")
	@Test
	void updateProduct_whenProductIsSavings_thenUpdateProduct() throws Exception {
		// given
		FinancialProduct product = FinancialProductDataProvider.createSavingsProduct(principalUser.getUser().getId(),
			InterestType.COMPOUND);
		financialProductRepository.save(product);

		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("수정된 적금 상품")
			.investmentType(InvestmentType.SAVINGS.name())
			.amount(BigDecimal.valueOf(2_000_000L))
			.months(12)
			.paymentDay(20)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.COMPOUND.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
			.startDate(LocalDate.of(2026, 2, 1))
			.build();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isNoContent());

		// 상품이 수정되었는지 검증
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(product.getId())) // ID는 변경되지 않아야 함
			.andExpect(jsonPath("$.name").value("수정된 적금 상품"))
			.andExpect(jsonPath("$.amount").value(2_000_000.0))
			.andExpect(jsonPath("$.startDate").value("2026-02-01"))
			.andExpect(jsonPath("$.paymentDay").value(20));
	}

	@DisplayName("상품 수정 - 사용자는 기존 현금 상품을 적금 상품으로 변경하지 못한다.")
	@Test
	void updateProduct_whenChangingInvestmentType_thenReturnBadRequest() throws Exception {
		// given
		FinancialProduct cash = FinancialProductDataProvider.createCashProduct(principalUser.getUser().getId());
		financialProductRepository.save(cash);

		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("적금 상품")
			.investmentType(InvestmentType.SAVINGS.name())
			.amount(BigDecimal.valueOf(1_000_000L))
			.months(12)
			.paymentDay(15)
			.interestRate(BigDecimal.valueOf(0.05))
			.interestType(InterestType.SIMPLE.name())
			.taxType(TaxType.STANDARD.name())
			.taxRate(BigDecimal.valueOf(0.154))
			.startDate(LocalDate.of(2026, 1, 1))
			.build();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", cash.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isBadRequest());
	}

	@DisplayName("상품 수정 - 다른 사용자가 생성한 상품을 수정하려고 하면 400 Bad Request를 반환한다")
	@Test
	void updateProduct_whenProductBelongsToAnotherUser_thenReturnBadRequest() throws Exception {
		// given
		String otherUserId = userIdGenerator.generateId();
		FinancialProduct product = FinancialProductDataProvider.createCashProduct(otherUserId);
		financialProductRepository.save(product);

		FinancialProductRequestDto dto = FinancialProductRequestDto.builder()
			.name("수정된 현금 상품")
			.investmentType(InvestmentType.CASH.name())
			.amount(BigDecimal.valueOf(2_000_000L))
			.months(0)
			.interestRate(BigDecimal.valueOf(0.00))
			.interestType(InterestType.NONE.name())
			.taxType(TaxType.NONE.name())
			.taxRate(BigDecimal.valueOf(0.0))
			.startDate(LocalDate.of(2026, 2, 1))
			.build();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Invalid request"));

		// 상품이 수정되지 않았는지 검증
		FinancialProduct findProduct = financialProductRepository.findByProductId(product.getId());
		Assertions.assertThat(findProduct.getName()).isEqualTo("현금 상품");
		Assertions.assertThat(findProduct.getAmount().getValue()).isEqualByComparingTo(BigDecimal.valueOf(1_000_000L));
		Assertions.assertThat(findProduct.getStartDate()).isEqualTo(LocalDate.of(2026, 1, 1));
	}

	@DisplayName("상품 삭제 - 사용자가 생성한 상품을 삭제한다")
	@Test
	void deleteProduct_whenProductExists_thenDeleteProduct() throws Exception {
		// given
		FinancialProduct product = FinancialProductDataProvider.createCashProduct(principalUser.getUser().getId());
		financialProductRepository.save(product);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isNoContent());

		// 상품이 삭제되었는지 검증
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Invalid request"));
	}

	@DisplayName("상품 삭제 - 다른 사용자가 생성한 상품을 삭제하려고 하면 400 Bad Request를 반환한다")
	@Test
	void deleteProduct_whenProductBelongsToAnotherUser_thenReturnBadRequest() throws Exception {
		// given
		String otherUserId = userIdGenerator.generateId();
		FinancialProduct product = FinancialProductDataProvider.createCashProduct(otherUserId);
		financialProductRepository.save(product);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", product.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Invalid request"));

		// 상품이 삭제되지 않았는지 검증
		Assertions.assertThat(financialProductRepository.findByProductId(product.getId()))
			.isNotNull();
	}

	@DisplayName("상품 삭제 - 존재하지 않는 상품을 삭제하려고 하면 400 Bad Request를 반환한다")
	@Test
	void deleteProduct_whenProductDoesNotExist_thenReturnBadRequest() throws Exception {
		// given
		String nonExistentProductId = productIdGenerator.generateId();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", nonExistentProductId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Invalid request"));
	}
}
