package co.invest72.transaction.domain;

import java.util.List;
import java.util.Optional;

import co.invest72.transaction.jpa.TransactionEntity;

public interface TransactionRepository {
	String save(TransactionEntity transaction);

	List<TransactionEntity> findByUserId(String userId);

	Optional<TransactionEntity> findBy(String transactionId, String userId);

	void deleteByIdAndUserId(List<String> transactionIds, String userId);

	void clear();
}
