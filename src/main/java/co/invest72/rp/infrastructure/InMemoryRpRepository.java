package co.invest72.rp.infrastructure;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.rp.entity.RepurchaseAgreementEntity;

public class InMemoryRpRepository implements RpRepository {
	private final Map<String, RepurchaseAgreementEntity> store = new ConcurrentHashMap<>();

	@Override
	public void save(RepurchaseAgreementEntity entity) {
		store.put(entity.getId(), entity);
	}

	@Override
	public Optional<RepurchaseAgreementEntity> findById(String id) {
		return Optional.ofNullable(store.get(id));
	}
}
