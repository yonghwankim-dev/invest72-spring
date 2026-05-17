package co.invest72.transaction.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.transaction.domain.TransactionRepository;
import co.invest72.transaction.domain.TransactionType;
import co.invest72.transaction.dto.TransactionDto;
import co.invest72.transaction.infrastructure.repository.InMemoryTransactionRepository;
import co.invest72.transaction.jpa.TransactionEntity;

class TransactionServiceTest {

	private TransactionService service;
	private TransactionRepository transactionRepository;
	private String userId;

	private List<TransactionDto> getTransactionDtos(String userId) {
		TransactionDto dto1 = TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(10_000))
			.content("책")
			.userId(userId)
			.build();
		TransactionDto dto2 = TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(20_000))
			.content("책2")
			.userId(userId)
			.build();
		TransactionDto dto3 = TransactionDto.builder()
			.type(TransactionType.INCOME.name())
			.amount(BigDecimal.valueOf(100_000))
			.content("용돈")
			.userId(userId)
			.build();
		TransactionDto dto4 = TransactionDto.builder()
			.type(TransactionType.INCOME.name())
			.amount(BigDecimal.valueOf(200_000))
			.content("용돈")
			.userId(userId)
			.build();
		TransactionDto dto5 = TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(20_000))
			.content("책2")
			.userId("user-4567")
			.build();
		TransactionDto dto6 = TransactionDto.builder()
			.type(TransactionType.INCOME.name())
			.amount(BigDecimal.valueOf(200_000))
			.content("용돈")
			.userId("user-4567")
			.build();
		return List.of(dto1, dto2, dto3, dto4, dto5, dto6);
	}

	@BeforeEach
	void setUp() {
		transactionRepository = new InMemoryTransactionRepository();
		service = new TransactionService(transactionRepository);
		userId = "user-1234";
	}

	@DisplayName("지출 거래 내역 생성")
	@Test
	void save_whenTypeIsExpense_thenSaveTransaction() {
		// given
		String userId = "user-1234";
		TransactionDto dto = TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(10_000))
			.content("책")
			.userId(userId)
			.build();
		// when
		service.save(dto);
		// then
		List<TransactionEntity> entities = transactionRepository.findByUserId(userId);
		Assertions.assertThat(entities).hasSize(1);
	}

	@DisplayName("지출 거래 내역 목록 조회")
	@Test
	void getTransactions_whenTypeIsExpense_thenReturnExpenseList() {
		// given
		getTransactionDtos(userId).forEach(service::save);
		// when
		List<TransactionDto> list = service.getTransactions(TransactionType.EXPENSE, userId);
		// then
		Assertions.assertThat(list).hasSize(2);
	}

	@DisplayName("수입 거래 내역 목록 조회")
	@Test
	void getTransactions_whenTypeIsIncome_thenReturnIncomeList() {
		// given
		getTransactionDtos(userId).forEach(service::save);
		// when
		List<TransactionDto> list = service.getTransactions(TransactionType.INCOME, userId);
		// then
		Assertions.assertThat(list).hasSize(2);
	}

	@DisplayName("거래 내역 수정 - 지출 거래 수정")
	@Test
	void updateTransaction_whenTypeIsExpense() {
		// given
		String transactionId = service.save(TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(10_000))
			.content("책")
			.userId(userId)
			.build());

		TransactionDto updateDto = TransactionDto.builder()
			.type(TransactionType.INCOME.name())
			.amount(BigDecimal.valueOf(20_000))
			.content("저녁")
			.userId(userId)
			.build();
		// when
		service.update(updateDto, transactionId);
		// then
		TransactionEntity expected = TransactionEntity.builder()
			.id(transactionId)
			.type(TransactionType.INCOME.name())
			.amount(BigDecimal.valueOf(20_000))
			.content("저녁")
			.userId(userId)
			.build();
		Assertions.assertThat(transactionRepository.findByTransactionId(transactionId)).contains(expected);
	}

	@DisplayName("거래 내역 수정 - 거래 내역에서 거래내역 식별자, 사용자 식별자, 생성시간을  수정할 수 없다")
	@Test
	void updateTransaction_whenChangeTransactionIdAndUserIdAndCreatedAt_thenNotChangedData() {
		// given
		TransactionDto dto = TransactionDto.builder()
			.type(TransactionType.EXPENSE.name())
			.amount(BigDecimal.valueOf(10_000))
			.content("책")
			.userId(userId)
			.build();
		String transactionId = service.save(dto);

		TransactionDto updateDto = TransactionDto.builder()
			.transactionId("transaction-4567")
			.type(TransactionType.INCOME.name())
			.amount(BigDecimal.valueOf(20_000))
			.content("저녁")
			.userId("user-4567") // other user id
			.createdAt(LocalDate.of(2026, 1, 1).atStartOfDay())
			.build();
		// when
		service.update(updateDto, transactionId);
		// then
		TransactionEntity actual = transactionRepository.findByTransactionId(transactionId).orElseThrow();
		Assertions.assertThat(actual.getId()).isNotEqualTo("transaction-4567");
		Assertions.assertThat(actual.getUserId()).isNotEqualTo("user-4567");
		Assertions.assertThat(actual.getCreatedAt()).isNotEqualTo(LocalDate.of(2026, 1, 1).atStartOfDay());
	}

	@DisplayName("거래 내역 삭제")
	@Test
	void delete() {
		// given
		List<TransactionDto> dtos = getTransactionDtos(userId);
		List<String> transactionIds = dtos.stream()
			.map(dto -> service.save(dto))
			.toList();
		// when
		service.delete(transactionIds, userId);
		// then
		Assertions.assertThat(transactionRepository.findByUserId(userId)).isEmpty();
	}

}
