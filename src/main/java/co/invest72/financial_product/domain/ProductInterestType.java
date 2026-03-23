package co.invest72.financial_product.domain;

import java.util.Objects;

import co.invest72.investment.domain.interest.InterestType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ProductInterestType {
	@Column(name = "interest_type", nullable = false, length = 100)
	private String name;

	protected ProductInterestType() {
	}

	private ProductInterestType(String name) {
		this.name = Objects.requireNonNull(name);
	}

	public static ProductInterestType from(String name) {
		return from(InterestType.valueOf(name));
	}

	public static ProductInterestType from(InterestType interestType) {
		return new ProductInterestType(interestType.name());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductInterestType that = (ProductInterestType)o;
		return this.name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
