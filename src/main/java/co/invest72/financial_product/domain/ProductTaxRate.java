package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductTaxRate {
	private static final int SCALE = 4;

	@Column(name = "tax_rate", nullable = false, precision = 5, scale = 4)
	private BigDecimal value;

	protected ProductTaxRate() {

	}

	public ProductTaxRate(BigDecimal value) {
		this.value = value;
		validate(this.value);
	}

	public BigDecimal getValue() {
		return this.value.stripTrailingZeros().setScale(SCALE, RoundingMode.HALF_EVEN);
	}

	private void validate(BigDecimal value) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(BigDecimal.ONE) >= 0) {
			throw new IllegalArgumentException("Tax rate must be between 0 and 1 (exclusive).");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductTaxRate that = (ProductTaxRate)o;
		BigDecimal value1 = this.value.stripTrailingZeros().setScale(SCALE, RoundingMode.HALF_EVEN);
		BigDecimal value2 = that.value.stripTrailingZeros().setScale(SCALE, RoundingMode.HALF_EVEN);
		return value1.compareTo(value2) == 0;
	}

	@Override
	public int hashCode() {
		return this.value.stripTrailingZeros().setScale(SCALE, RoundingMode.HALF_EVEN).hashCode();
	}
}
