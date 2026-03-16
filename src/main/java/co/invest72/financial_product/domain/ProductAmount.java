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

	private static final BigDecimal MAX_AMOUNT = new BigDecimal("10000000000000"); // 10조원

	@Column(name = "amount", nullable = false, precision = 19, scale = 2)
	private BigDecimal value;

	@Column(name = "currency", nullable = false, length = 3)
	private String currency;

	/**
	 * 금액은 0원 이상이어야 하며, 10조원을 초과할 수 없습니다.
	 *
	 * @param value 금액
	 * @throws IllegalArgumentException 유효하지 않은 금액인 경우
	 */
	public ProductAmount(BigDecimal value) {
		this.value = value;
		// TODO: 다양한 통화 지원을 위해 currency 필드를 추가하고, 생성자에서 통화도 함께 받도록 변경할 수 있습니다.
		this.currency = "KRW"; // 기본 통화는 원화로 설정
		validate(this.value);
	}

	private void validate(BigDecimal value) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
		}
		if (value.compareTo(MAX_AMOUNT) > 0) {
			throw new IllegalArgumentException("금액은 10조원을 초과할 수 없습니다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductAmount that = (ProductAmount)o;
		return value.compareTo(that.value) == 0;
	}

	@Override
	public int hashCode() {
		if (value == null) {
			return 0;
		}
		return value.stripTrailingZeros().hashCode();
	}
}
