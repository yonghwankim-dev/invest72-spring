package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ProductAnnualInterestRate {
	private static final BigDecimal MAX_RATE = new BigDecimal("9.99999999");

	/**
	 * -- GETTER --
	 *  DB 및 엔티티에 저장된 '원본 그대로의 수치(Raw Value)'를 반환합니다.
	 *  이 단계에서는 임의의 setScale이나 반올림을 가하지 않습니다.
	 */
	@Column(name = "interest_rate", nullable = false, precision = 9, scale = 8)
	private BigDecimal value;

	protected ProductAnnualInterestRate() {
	}

	public ProductAnnualInterestRate(BigDecimal value) {
		this.value = value;
		validate(this.value);
	}

	private void validate(BigDecimal value) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(MAX_RATE) > 0) {
			throw new IllegalArgumentException("값은 0 이상 " + MAX_RATE + " 이하이어야 합니다.");
		}
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof ProductAnnualInterestRate that))
			return false;
		// BigDecimal의 scale 차이를 무시하고 수치적 가치(value)가 같은지 비교
		return this.value != null && that.value != null && this.value.compareTo(that.value) == 0;
	}

	@Override
	public int hashCode() {
		// compareTo 결과와 일치하도록 stripTrailingZeros 후 hashCode 생성
		return Objects.hash(value != null ? value.stripTrailingZeros() : null);
	}

	@Override
	public String toString() {
		return "value=" + value;
	}
}
