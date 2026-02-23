package co.invest72.financial_product.presentation;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.fasterxml.jackson.databind.ObjectMapper;

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

	private PrincipalUser principalUser;

	@BeforeEach
	void setUp() {
		String id = idGenerator.generateId();
		String email = "user1@gmail.com";
		String nickname = "user1";
		String providerId = idGenerator.generateId();
		User testUser = new User(id, email, nickname, providerId);
		principalUser = new PrincipalUser(testUser, null, null, null);
	}

	@Test
	@DisplayName("상품 생성 - 현금 상품을 성공적으로 생성한다")
	void createProduct_whenProductTypeIsCash_thenSaveProduct() throws Exception {
		// given
		CreateFinancialProductDto dto = CreateFinancialProductDto.builder()
			.name("현금 상품")
			.productType(ProductType.CASH.name())
			.amount(1_000_000L)
			.months(0)
			.interestRate(0.0)
			.interestType(InterestType.NONE.name())
			.taxType(TaxType.NON_TAX.name())
			.taxRate(0.0)
			.build();
		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(notNullValue()));

	}
}
