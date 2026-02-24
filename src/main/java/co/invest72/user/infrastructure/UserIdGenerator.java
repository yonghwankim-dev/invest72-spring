package co.invest72.user.infrastructure;

import java.util.UUID;

import co.invest72.financial_product.domain.IdGenerator;

public class UserIdGenerator implements IdGenerator {

	private final String prefix;

	public UserIdGenerator(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 사용자 ID를 생성하여 반환한다
	 * 사용자 ID는 고유해야 하며, 시스템에서 관리되는 식별자로 사용된다.
	 * ID 형식 : prefix + "-" + UUID
	 * 예시: "user-550e8400-e29b-41d4-a716-446655440000"
	 * @return 생성된 사용자 ID
	 */
	@Override
	public String generateId() {
		return prefix + "-" + UUID.randomUUID();
	}
}
