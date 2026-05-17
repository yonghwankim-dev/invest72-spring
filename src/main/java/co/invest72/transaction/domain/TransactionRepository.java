package co.invest72.transaction.domain;

import java.util.List;
import java.util.Optional;

import co.invest72.transaction.jpa.TransactionEntity;

public interface TransactionRepository {
	String save(TransactionEntity transaction);

	List<TransactionEntity> findByUserId(String userId);

	Optional<TransactionEntity> findByTransactionId(String transactionId);

	void deleteByIdAndUserId(List<String> transactionIds, String userId);
}
