package co.invest72.financial_product.domain;

import java.util.Objects;

import co.invest72.investment.domain.investment.InvestmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ProductInvestmentType {
	@Column(name = "investment_type", nullable = false, length = 100)
	private String name;

	protected ProductInvestmentType() {
	}

	private ProductInvestmentType(String name) {
		this.name = Objects.requireNonNull(name);
	}

	public static ProductInvestmentType from(String name) {
		return from(InvestmentType.valueOf(name));
	}

	public static ProductInvestmentType from(InvestmentType investmentType) {
		return new ProductInvestmentType(investmentType.name());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductInvestmentType that = (ProductInvestmentType)o;
		return this.name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
