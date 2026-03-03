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

	/**
	 * ID로 금융 상품을 조회합니다.
	 * ID로 조회된 금융 상품이 존재하지 않으면 null을 반환합니다.
	 * @param id 조회할 금융 상품의 ID
	 * @return 조회된 금융 상품, 존재하지 않으면 null
	 */
	@Override
	public FinancialProduct findByProductId(String id) {
		return jpaRepository.findById(id)
			.orElse(null);
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
