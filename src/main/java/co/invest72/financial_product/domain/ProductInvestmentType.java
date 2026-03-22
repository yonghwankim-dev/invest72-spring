package co.invest72.financial_product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductInvestmentType {
	@Column(name = "product_investment_type", nullable = false, length = 100)
	private String name;

	protected ProductInvestmentType() {
	}

	public ProductInvestmentType(String name) {
		this.name = name;
	}
}
