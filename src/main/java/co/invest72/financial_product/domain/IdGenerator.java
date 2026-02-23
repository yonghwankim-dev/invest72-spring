package co.invest72.financial_product.domain;

public interface IdGenerator {
	/**
	 * 고유한 ID를 생성합니다.
	 * @return 생성된 ID
	 */
	String generateId();
}
