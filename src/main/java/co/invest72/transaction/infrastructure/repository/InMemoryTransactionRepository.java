package co.invest72.transaction.infrastructure.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.transaction.domain.TransactionRepository;
import co.invest72.transaction.jpa.TransactionEntity;

public class InMemoryTransactionRepository implements TransactionRepository {

	private final Map<String, TransactionEntity> store;

	public InMemoryTransactionRepository() {
		this.store = new ConcurrentHashMap<>();
	}

	@Override
	public String save(TransactionEntity transaction) {
		store.put(transaction.getId(), transaction);
		return transaction.getId();
	}

	@Override
	public List<TransactionEntity> findByUserId(String userId) {
		return store.values().stream()
			.filter(t -> t.getUserId().equals(userId))
			.toList();
	}

	@Override
	public Optional<TransactionEntity> findByTransactionId(String transactionId) {
		return Optional.ofNullable(store.get(transactionId));
	}
}
