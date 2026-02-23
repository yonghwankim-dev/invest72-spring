package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAmount {

	@Column(name = "amount", nullable = false, precision = 19, scale = 2)
	private BigDecimal value;

	/**
	 * 금액은 0원 이상이어야 하며, 10조원을 초과할 수 없습니다.
	 *
	 * @param value 금액
	 * @throws IllegalArgumentException 유효하지 않은 금액인 경우
	 */
	public ProductAmount(BigDecimal value) {
		this.value = value;
		validate(this.value);
	}

	private void validate(BigDecimal value) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
		}
		if (value.compareTo(new BigDecimal("10000000000000")) > 0) {
			throw new IllegalArgumentException("금액은 10조원을 초과할 수 없습니다.");
		}
	}
}
