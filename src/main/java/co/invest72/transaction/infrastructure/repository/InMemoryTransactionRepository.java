package co.invest72.transaction.infrastructure.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.transaction.domain.TransactionRepository;
import co.invest72.transaction.domain.TransactionType;
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

	@Override
	public List<TransactionEntity> findByUserId(String userId) {
		return store.values().stream()
			.filter(t -> t.getUserId().equals(userId))
			.toList();
	}

	@Override
	public List<TransactionEntity> findExpenseTransactionByUserId(String userId) {
		return store.values().stream()
			.filter(t -> t.getUserId().equals(userId))
			.filter(t -> t.getType().equals(TransactionType.EXPENSE.name()))
			.toList();
	}
}
