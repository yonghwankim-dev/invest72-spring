package co.invest72.financial_product.domain;

import co.invest72.investment.domain.tax.TaxType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductTaxType {
	@Column(name = "product_tax_type", nullable = false, length = 100)
	private String name;

	protected ProductTaxType() {
	}

	private ProductTaxType(String name) {
		this.name = name;
	}
	
	public static ProductTaxType from(TaxType taxType) {
		return new ProductTaxType(taxType.name());
	}
}
