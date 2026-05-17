package co.invest72.transaction.application;

import java.util.List;

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
	public void save(TransactionDto dto) {
		TransactionEntity entity = TransactionEntity.builder()
			.type(dto.getType())
			.amount(dto.getAmount())
			.content(dto.getContent())
			.userId(dto.getUserId())
			.build();
		repository.save(entity);
	}
	
	@Transactional(readOnly = true)
	public List<TransactionDto> getTransactions(TransactionType type, String userId) {
		return repository.findByUserId(userId).stream()
			.filter(t -> t.getType().equalsIgnoreCase(type.name()))
			.map(t -> TransactionDto.builder()
				.type(TransactionType.valueOf(t.getType()))
				.amount(t.getAmount())
				.content(t.getContent())
				.userId(t.getUserId())
				.build()
			)
			.toList();
	}
}
