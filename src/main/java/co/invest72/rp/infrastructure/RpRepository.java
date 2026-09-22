package co.invest72.rp.infrastructure;

import java.util.Optional;

import co.invest72.rp.entity.RepurchaseAgreementEntity;

public interface RpRepository {
	void save(RepurchaseAgreementEntity entity);

	Optional<RepurchaseAgreementEntity> findById(String id);
}
