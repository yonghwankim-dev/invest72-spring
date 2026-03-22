package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ProductTaxRate {

	@Column(name = "tax_rate", nullable = false, precision = 5, scale = 4)
	private BigDecimal value;

	protected ProductTaxRate() {

	}

	public ProductTaxRate(BigDecimal value) {
		this.value = value;
		validate(this.value);
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
		return this.value.compareTo(that.value) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
