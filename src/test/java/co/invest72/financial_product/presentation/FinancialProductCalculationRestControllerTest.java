package co.invest72.financial_product.presentation;

import static co.invest72.investment.domain.interest.InterestType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.financial_product.domain.ProductAmount;
import co.invest72.financial_product.domain.ProductMonths;
import co.invest72.financial_product.domain.ProductRate;
import co.invest72.financial_product.infrastructure.ProductIdGenerator;
import co.invest72.investment.domain.investment.InvestmentType;
import co.invest72.investment.domain.tax.TaxType;
import co.invest72.security.PrincipalUser;
import co.invest72.user.domain.User;
import co.invest72.user.infrastructure.UserIdGenerator;

@SpringBootTest
@AutoConfigureMockMvc
class FinancialProductCalculationRestControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private FinancialProductRepository financialProductRepository;

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
	}

	@AfterEach
	void tearDown() {
		financialProductRepository.clear();
	}

	@DisplayName("상품 수익 계산 - 단리-예금")
	@Test
	void calculateFinancialProduct_whenProductIsSimpleDeposit_thenReturnsCalculationResult() throws Exception {
		// Given
		FinancialProduct product = FinancialProduct.builder()
			.userId(principalUser.getUser().getId())
			.name("단리-예금")
			.investmentType(InvestmentType.DEPOSIT)
			.amount(new ProductAmount(BigDecimal.valueOf(1_000_000)))
			.months(new ProductMonths(12))
			.interestRate(new ProductRate(BigDecimal.valueOf(0.05)))
			.interestType(SIMPLE)
			.taxType(TaxType.NON_TAX)
			.taxRate(new ProductRate(BigDecimal.ZERO))
			.startDate(LocalDate.now())
			.createdAt(LocalDateTime.now())
			.build();
		financialProductRepository.save(product);
		String productId = product.getId();

		// When & Then
		mockMvc.perform(get("/api/v1/products/{id}/calculate", productId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser)))
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
	}
}
