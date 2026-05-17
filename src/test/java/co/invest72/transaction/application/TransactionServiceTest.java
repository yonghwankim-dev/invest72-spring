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
}
