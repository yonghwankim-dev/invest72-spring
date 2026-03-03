package co.invest72.financial_product.infrastructure.repository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.FinancialProductRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Primary
public class FinancialProductRepositoryImpl implements FinancialProductRepository {

	private final JpaFinancialProductRepository jpaRepository;

	@Override
	public String save(FinancialProduct product) {
		return jpaRepository.save(product).getId();
	}

	@Override
	public FinancialProduct findByProductId(String id) {
		return jpaRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("금융 상품을 찾을 수 없습니다. ID: " + id));

	}

	@Override
	public List<FinancialProduct> findAllByUserId(String userId) {
		return jpaRepository.findByUserId(userId);
	}

	@Override
	public void deleteByProductId(String productId) {
		jpaRepository.deleteById(productId);
	}

	@Override
	public void clear() {
		jpaRepository.deleteAll();
	}
}
