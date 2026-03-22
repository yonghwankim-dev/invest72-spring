package co.invest72.financial_product.domain;

import co.invest72.investment.domain.interest.InterestType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductInterestType {
	// TODO: columnNmae will do "product_interst_type to interest_type"
	@Column(name = "product_interest_type", nullable = false, length = 100)
	private String name;

	protected ProductInterestType() {
	}

	private ProductInterestType(String name) {
		this.name = name;
	}

	public static ProductInterestType from(InterestType interestType) {
		return new ProductInterestType(interestType.name());
	}
}
