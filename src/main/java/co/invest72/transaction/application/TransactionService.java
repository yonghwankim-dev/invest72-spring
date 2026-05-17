package co.invest72.transaction.application;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.transaction.domain.TransactionRepository;
import co.invest72.transaction.dto.TransactionDto;
import co.invest72.transaction.jpa.TransactionEntity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository repository;
	private final ModelMapper modelMapper;

	@Transactional
	public void save(TransactionDto dto) {
		TransactionEntity entity = modelMapper.map(dto, TransactionEntity.class).toBuilder()
			.id("transaction-" + UUID.randomUUID())
			.build();
		repository.save(entity);
	}
}
