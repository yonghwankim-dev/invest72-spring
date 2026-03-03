package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRate {

	private static final BigDecimal MAX_RATE = new BigDecimal("9.9999");

	@Column(nullable = false, precision = 5, scale = 4)
	private BigDecimal value;

	public ProductRate(BigDecimal value) {
		this.value = value;
		validate(this.value);
	}

	private void validate(BigDecimal value) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(MAX_RATE) > 0) {
			BigDecimal maxRatePercent = MAX_RATE.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
			throw new IllegalArgumentException("금리는 0% 이상 " + maxRatePercent + "% 이하이어야 합니다.");
		}
	}

	@Override
	public String toString() {
		return "ProductRate=%s%%".formatted(value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductRate that = (ProductRate)o;
		return value.compareTo(that.value) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
