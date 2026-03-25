package co.invest72.financial_product.domain;

import java.util.Objects;

import co.invest72.investment.domain.tax.TaxType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ProductTaxType {
	@Column(name = "tax_type", nullable = false, length = 100)
	private String name;

	protected ProductTaxType() {
	}

	private ProductTaxType(String name) {
		this.name = Objects.requireNonNull(name);
	}

	public static ProductTaxType from(String name) {
		return from(TaxType.valueOf(name));
	}

	public static ProductTaxType from(TaxType taxType) {
		return new ProductTaxType(taxType.name());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductTaxType that = (ProductTaxType)o;
		return this.name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
