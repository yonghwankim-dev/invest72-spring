package co.invest72.financial_product.infrastructure;

import java.util.UUID;

import co.invest72.financial_product.domain.IdGenerator;

public class ProductIdGenerator implements IdGenerator {

	private final String prefix;

	public ProductIdGenerator(String prefix) {
		this.prefix = prefix;
		if (prefix == null || prefix.isEmpty()) {
			throw new IllegalArgumentException("Prefix cannot be null or empty");
		}
	}

	/**
	 * 상품 ID를 생성하여 반환한다
	 * 상품 ID는 고유해야 하며, 시스템에서 관리되는 식별자로 사용된다.
	 * ID 형식 : prefix + "-" + UUID
	 * @return 생성된 상품 ID
	 */
	@Override
	public String generateId() {
		return prefix + "-" + UUID.randomUUID();
	}
}
