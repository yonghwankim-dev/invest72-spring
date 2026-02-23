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
public class ProductRate {

	@Column(nullable = false, precision = 5, scale = 4)
	private BigDecimal value;

	public ProductRate(BigDecimal value) {
		this.value = value;
		validate(this.value);
	}

	private void validate(BigDecimal value) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal("9.9999")) > 0) {
			throw new IllegalArgumentException("이율/세율은 0.0에서 9.9999 사이여야 합니다.");
		}
	}

	@Override
	public String toString() {
		return "ProductRate=%s%%".formatted(value);
	}
}
