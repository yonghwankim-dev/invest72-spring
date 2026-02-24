package co.invest72.financial_product.infrastructure.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;

@Repository
public class InMemoryFinancialProductRepository implements FinancialProductRepository {
	private final Map<String, FinancialProduct> storage = new ConcurrentHashMap<>();

	@Override
	public String save(FinancialProduct product) {
		storage.put(product.getId(), product);
		return product.getId();
	}

	@Override
	public FinancialProduct findByProductId(String productId) {
		return storage.get(productId);
	}

	@Override
	public List<FinancialProduct> findAllByUserId(String userId) {
		return storage.values().stream()
			.filter(product -> product.getUserId().equals(userId))
			.toList();
	}

	@Override
	public void deleteByProductId(String productId) {
		storage.remove(productId);
	}

	@Override
	public void clear() {
		storage.clear();
	}
}
