package co.invest72.transaction.infrastructure.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.transaction.domain.TransactionRepository;
import co.invest72.transaction.jpa.TransactionEntity;

public class InMemoryTransactionRepository implements TransactionRepository {

	private final Map<String, TransactionEntity> store;

	public InMemoryTransactionRepository() {
		this.store = new ConcurrentHashMap<>();
	}

	@Override
	public void save(TransactionEntity transaction) {
		store.put(transaction.getId(), transaction);
	}
}
