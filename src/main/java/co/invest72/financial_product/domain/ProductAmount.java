package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import co.invest72.money.domain.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAmount {

	private static final BigDecimal MAX_AMOUNT = new BigDecimal("99999999999999999"); // 99999조

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "amount", nullable = false, precision = 19, scale = 2))
	@AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
	private Money value;

	/**
	 * 금액을 나타내는 객체를 생성한다. 금액은 0원 이상 99999조원 이하이어야 한다.
	 *
	 * @param value 금액
	 * @throws IllegalArgumentException 유효하지 않은 금액인 경우
	 */
	public ProductAmount(BigDecimal value) {
		this.value = Money.won(value);
		validate(this.value);
	}

	private void validate(Money value) {
		if (value == null || value.compareTo(Money.won(BigDecimal.ZERO)) < 0) {
			throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
		}
		if (value.compareTo(Money.won(MAX_AMOUNT)) > 0) {
			throw new IllegalArgumentException("금액은 99999조원을 초과할 수 없습니다.");
		}
	}

	public BigDecimal getValue() {
		return value.getValue();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductAmount that = (ProductAmount)o;
		return this.value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
