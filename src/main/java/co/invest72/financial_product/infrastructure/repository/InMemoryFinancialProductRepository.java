package co.invest72.financial_product.infrastructure.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import co.invest72.financial_product.domain.IdGenerator;

@Repository
public class InMemoryFinancialProductRepository implements FinancialProductRepository {
	private final Map<String, FinancialProduct> storage = new ConcurrentHashMap<>();
	private final IdGenerator idGenerator;

	public InMemoryFinancialProductRepository(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Override
	public String save(FinancialProduct product) {
		String id = idGenerator.generateId();
		FinancialProduct newProduct = product.toBuilder()
			.id(id)
			.build();
		storage.put(id, newProduct);
		return id;
	}

	@Override
	public FinancialProduct findById(String id) {
		return storage.get(id);
	}

	@Override
	public List<FinancialProduct> findAllById(String id) {
		return storage.values().stream()
			.filter(product -> product.getUserId().equals(id))
			.toList();
	}

	@Override
	public void clear() {
		storage.clear();
	}
}
