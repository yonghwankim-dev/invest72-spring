package co.invest72.transaction.application;

import java.math.BigDecimal;
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

	@BeforeEach
	void setUp() {
		transactionRepository = new InMemoryTransactionRepository();
		service = new TransactionService(transactionRepository);
	}

	@DisplayName("지출 거래 생성")
	@Test
	void save_whenTypeIsExpense_thenSaveTransaction() {
		// given
		String userId = "user-1234";
		TransactionDto dto = new TransactionDto(TransactionType.EXPENSE, BigDecimal.valueOf(10000), "책", userId);
		// when
		service.save(dto);
		// then
		List<TransactionEntity> entities = transactionRepository.findByUserId(userId);
		Assertions.assertThat(entities).hasSize(1);
	}

	@DisplayName("지출 거래 목록 조회")
	@Test
	void getTransactions_whenTypeIsExpense_thenReturnExpenseList() {
		// given
		String userId = "user-1234";
		service.save(new TransactionDto(TransactionType.EXPENSE, BigDecimal.valueOf(10_000), "책", userId));
		service.save(new TransactionDto(TransactionType.EXPENSE, BigDecimal.valueOf(20_000), "책2", userId));
		service.save(new TransactionDto(TransactionType.INCOME, BigDecimal.valueOf(100_000), "용돈", userId));

		String otherUserId = "user-4567";
		service.save(new TransactionDto(TransactionType.EXPENSE, BigDecimal.valueOf(20_000), "책2", otherUserId));
		// when
		List<TransactionDto> list = service.getTransactions(TransactionType.EXPENSE, userId);
		// then
		Assertions.assertThat(list).hasSize(2);
	}

	@DisplayName("수입 거래 목록 조회")
	@Test
	void getTransactions_whenTypeIsIncome_thenReturnIncomeList() {
		// given
		String userId = "user-1234";
		service.save(new TransactionDto(TransactionType.EXPENSE, BigDecimal.valueOf(10_000), "책", userId));
		service.save(new TransactionDto(TransactionType.EXPENSE, BigDecimal.valueOf(20_000), "책2", userId));
		service.save(new TransactionDto(TransactionType.INCOME, BigDecimal.valueOf(100_000), "용돈", userId));
		service.save(new TransactionDto(TransactionType.INCOME, BigDecimal.valueOf(200_000), "용돈", userId));

		String otherUserId = "user-4567";
		service.save(new TransactionDto(TransactionType.EXPENSE, BigDecimal.valueOf(20_000), "책2", otherUserId));
		service.save(new TransactionDto(TransactionType.INCOME, BigDecimal.valueOf(200_000), "용돈", otherUserId));
		// when
		List<TransactionDto> list = service.getTransactions(TransactionType.INCOME, userId);
		// then
		Assertions.assertThat(list).hasSize(2);
	}

	@DisplayName("거래 수정 - 지출 거래 수정")
	@Test
	void updateTransaction_whenTypeIsExpense() {
		// given
		String userId = "user-1234";
		TransactionDto dto = new TransactionDto(TransactionType.EXPENSE, BigDecimal.valueOf(10_000), "책", userId);
		String transactionId = service.save(dto);

		TransactionDto updateDto = TransactionDto.builder()
			.type(TransactionType.INCOME)
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
}
