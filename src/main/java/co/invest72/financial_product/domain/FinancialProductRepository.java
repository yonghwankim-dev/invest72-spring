package co.invest72.financial_product.domain;

import java.util.List;

public interface FinancialProductRepository {
	/**
	 * 금융 상품을 저장합니다.
	 *
	 * @param product 저장할 금융 상품
	 * @return 저장된 금융 상품의 ID
	 */
	String save(FinancialProduct product);

	/**
	 * ID로 금융 상품을 조회합니다.
	 *
	 * @param id 조회할 금융 상품의 ID
	 * @return 조회된 금융 상품, 존재하지 않으면 null
	 */
	FinancialProduct findById(String id);

	List<FinancialProduct> findAllById(String id);
}
