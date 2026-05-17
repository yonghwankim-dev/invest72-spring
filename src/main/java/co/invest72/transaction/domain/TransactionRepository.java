package co.invest72.transaction.domain;

import java.util.List;

import co.invest72.transaction.jpa.TransactionEntity;

public interface TransactionRepository {
	void save(TransactionEntity transaction);

	List<TransactionEntity> findByUserId(String userId);

	List<TransactionEntity> findExpenseTransactionByUserId(String userId);

	List<TransactionEntity> findIncomeTransactionByUserId(String userId);
}
