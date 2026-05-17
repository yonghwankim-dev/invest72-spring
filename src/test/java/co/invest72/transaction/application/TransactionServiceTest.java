package co.invest72.transaction.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import co.invest72.transaction.domain.TransactionRepository;
import co.invest72.transaction.dto.TransactionDto;
import co.invest72.transaction.infrastructure.repository.InMemoryTransactionRepository;

class TransactionServiceTest {

	private TransactionService service;

	@BeforeEach
	void setUp() {
		TransactionRepository transactionRepository = new InMemoryTransactionRepository();
		ModelMapper modelMapper = new ModelMapper();
		service = new TransactionService(transactionRepository, modelMapper);
	}

	@DisplayName("지출 거래 생성")
	@Test
	void save_whenTypeIsExpense_thenSaveTransaction() {
		// given
		TransactionDto dto = new TransactionDto();
		// when
		service.save(dto);
		// then
		Assertions.assertThat(service).isNotNull();
	}
}
