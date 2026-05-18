package co.invest72.transaction.presentation;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
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
import co.invest72.transaction.application.TransactionService;
import co.invest72.transaction.domain.TransactionType;
import co.invest72.transaction.dto.TransactionDto;
import co.invest72.transaction.presentation.vo.TransactionDeleteRequest;
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

	@Autowired
	private TransactionService service;

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

	@Test
	@DisplayName("거래 내역 목록 조회")
	void getTransactions_whenTypeIsExpense_thenReturnList() throws Exception {
		// given
		TransactionDto dto = TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(10_000))
			.content("책")
			.userId(principalUser.getUser().getId())
			.build();
		service.save(dto);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transactions")
				.queryParam("type", TransactionType.EXPENSE.name())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.transactions").isArray())
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	@DisplayName("특정 거래 내역 조회")
	void getDetailedTransaction() throws Exception {
		// given
		TransactionDto dto = TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(10_000))
			.content("책")
			.userId(principalUser.getUser().getId())
			.build();
		String transactionId = service.save(dto);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transactions/{transactionId}", transactionId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("transactionId").value(equalTo(transactionId)))
			.andExpect(jsonPath("type").value(equalTo(dto.getType())))
			.andExpect(jsonPath("amount").value(equalTo(dto.getAmount().intValue())))
			.andExpect(jsonPath("content").value(equalTo(dto.getContent())))
			.andExpect(jsonPath("createdAt").value(notNullValue()))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	@DisplayName("거래 내역 수정")
	void updateTransaction() throws Exception {
		// given
		TransactionDto dto = TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(10_000))
			.content("책")
			.userId(principalUser.getUser().getId())
			.build();
		String saveTransactionId = service.save(dto);

		TransactionRequest request = TransactionRequest.builder()
			.type(TransactionType.INCOME.name())
			.amount(BigDecimal.valueOf(20_000))
			.content("용돈")
			.build();

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/transactions/{transactionId}", saveTransactionId)
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
		TransactionDto updatedDto = service.getTransactions(TransactionType.INCOME, principalUser.getUser().getId())
			.stream()
			.findAny()
			.orElseThrow();
		Assertions.assertThat(updatedDto.getType()).isEqualTo(TransactionType.INCOME.name());
		Assertions.assertThat(updatedDto.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(20_000));
		Assertions.assertThat(updatedDto.getContent()).isEqualTo("용돈");
	}

	@Test
	@DisplayName("거래 내역 삭제")
	void deleteTransactions_whenMultipleTransactions_thenDeleteData() throws Exception {
		// given
		TransactionDto dto = TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(10_000))
			.content("책")
			.userId(principalUser.getUser().getId())
			.build();
		String saveTransactionId1 = service.save(dto);
		String saveTransactionId2 = service.save(dto);
		List<String> transactionIds = Arrays.asList(saveTransactionId1, saveTransactionId2);
		TransactionDeleteRequest request = new TransactionDeleteRequest(transactionIds);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/transactions")
				.queryParam("type", TransactionType.EXPENSE.name())
				.with(SecurityMockMvcRequestPostProcessors.user(principalUser))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent())
			.andDo(MockMvcResultHandlers.print());
		Assertions.assertThat(service.getTransactions(TransactionType.EXPENSE, principalUser.getUser().getId()))
			.isEmpty();
	}
}
