package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ProductTaxRate {

	@Column(name = "product_tax_rate", nullable = false, precision = 5, scale = 4)
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
}
