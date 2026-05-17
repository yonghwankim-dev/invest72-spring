package co.invest72.transaction.application;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.transaction.domain.TransactionRepository;
import co.invest72.transaction.domain.TransactionType;
import co.invest72.transaction.dto.TransactionDto;
import co.invest72.transaction.jpa.TransactionEntity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository repository;

	@Transactional
	public String save(TransactionDto dto) {
		TransactionEntity entity = TransactionEntity.builder()
			.type(dto.getType())
			.amount(dto.getAmount())
			.content(dto.getContent())
			.userId(dto.getUserId())
			.build();
		return repository.save(entity);
	}

	@Transactional(readOnly = true)
	public List<TransactionDto> getTransactions(TransactionType type, String userId) {
		return repository.findByUserId(userId).stream()
			.filter(t -> t.getType().equalsIgnoreCase(type.name()))
			.map(t -> TransactionDto.builder()
				.type(t.getType())
				.amount(t.getAmount())
				.content(t.getContent())
				.userId(t.getUserId())
				.build()
			)
			.toList();
	}

	@Transactional
	public void update(TransactionDto updateDto, String transactionId) {
		TransactionEntity originalEntity = repository.findByTransactionId(transactionId)
			.orElseThrow(() -> new NoSuchElementException("not found transaction, transactionId=" + transactionId));
		TransactionDto dto = updateDto.withTransactionId(originalEntity.getId())
			.withUserId(originalEntity.getUserId())
			.withCreatedAt(originalEntity.getCreatedAt());

		TransactionEntity updatedEntity = TransactionEntity.builder()
			.id(dto.getTransactionId())
			.type(dto.getType())
			.amount(dto.getAmount())
			.content(dto.getContent())
			.userId(dto.getUserId())
			.createdAt(dto.getCreatedAt())
			.build();

		originalEntity.update(updatedEntity);
	}

	@Transactional
	public void delete(List<String> transactionIds, String userId) {
		repository.deleteByIdAndUserId(transactionIds, userId);
	}
}
