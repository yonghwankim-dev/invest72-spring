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
	public Optional<TransactionEntity> findBy(String transactionId, String userId) {
		return Optional.ofNullable(store.get(transactionId))
			.filter(t -> t.getUserId().equalsIgnoreCase(userId));
	}

	@Override
	public void deleteByIdAndUserId(List<String> transactionIds, String userId) {
		for (String id : transactionIds) {
			TransactionEntity entity = store.get(id);
			if (entity.getUserId().equalsIgnoreCase(userId)) {
				store.remove(id);
			}
		}
	}

	@Override
	public void clear() {
		store.clear();
	}
}
