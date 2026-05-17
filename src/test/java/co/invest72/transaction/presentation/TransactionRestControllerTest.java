package co.invest72.transaction.presentation;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.invest72.security.PrincipalUser;
import co.invest72.transaction.domain.TransactionType;
import co.invest72.transaction.presentation.vo.TransactionRequest;
import co.invest72.user.domain.User;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private PrincipalUser principalUser;

	@BeforeEach
	void setUp() {
		String email = "user1@gmail.com";
		String nickname = "user1";
		String providerId = UUID.randomUUID().toString();
		User testUser = new User(email, nickname, providerId);
		principalUser = PrincipalUser.of()
			.user(testUser)
			.build();
	}

	@Test
	@DisplayName("거래 내역 생성 - 지출 거래 내역을 성공적으로 등록한다")
	void saveTransaction_whenTypeIsExpense_thenResponseTransactionId() throws Exception {
		// given
		TransactionRequest request = TransactionRequest.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(10_000))
			.content("책")
			.build();
		// when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions")
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.transactionId").value(notNullValue()))
			.andDo(MockMvcResultHandlers.print());
	}
}
