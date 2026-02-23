package co.invest72.financial_product.presentation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.financial_product.domain.ProductType;
import co.invest72.investment.domain.interest.InterestType;
import co.invest72.investment.domain.tax.TaxType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateFinancialProductRequest {
	private String name;
	private String productType;
	private Long amount;
	private Integer months;
	private Double interestRate;
	private String interestType;
	private String taxType;
	private Double taxRate;
	private LocalDate startDate;

	public FinancialProduct toEntity() {
		return FinancialProduct.builder()
			.name(name)
			.productType(ProductType.valueOf(productType))
			.amount(BigDecimal.valueOf(amount))
			.months(months)
			.interestRate(BigDecimal.valueOf(interestRate))
			.interestType(InterestType.valueOf(interestType))
			.taxType(TaxType.valueOf(taxType))
			.taxRate(BigDecimal.valueOf(taxRate))
			.build();
	}
}
