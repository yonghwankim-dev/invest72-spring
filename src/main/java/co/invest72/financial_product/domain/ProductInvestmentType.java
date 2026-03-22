package co.invest72.financial_product.domain;

import co.invest72.investment.domain.investment.InvestmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductInvestmentType {
	@Column(name = "product_investment_type", nullable = false, length = 100)
	private String name;

	protected ProductInvestmentType() {
	}

	private ProductInvestmentType(String name) {
		this.name = name;
	}

	public static ProductInvestmentType from(String name) {
		return from(InvestmentType.valueOf(name));
	}

	public static ProductInvestmentType from(InvestmentType investmentType) {
		return new ProductInvestmentType(investmentType.name());
	}
}
