package co.invest72.financial_product.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ProductAnnualInterestRate {
	@Column(nullable = false, precision = 5, scale = 4)
	private BigDecimal value;

	protected ProductAnnualInterestRate() {
	}

	public ProductAnnualInterestRate(BigDecimal value) {
		this.value = value;
	}
}
