package co.invest72.transaction.domain;

import co.invest72.transaction.jpa.TransactionEntity;

public interface TransactionRepository {
	void save(TransactionEntity transaction);
}
