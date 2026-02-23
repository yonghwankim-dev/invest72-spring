package co.invest72.financial_product.presentation.dto.response;

import co.invest72.financial_product.domain.FinancialProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ProductResponseDto {

	private String id;
	private String userId;
	private String name;
	private String productType;
	private double amount;
	private int months;
	private double interestRate;
	private String interestType;
	private String taxType;
	private double taxRate;

	public static ProductResponseDto from(FinancialProduct product) {
		return ProductResponseDto.builder()
			.id(product.getId())
			.userId(product.getUser().getId())
			.name(product.getName())
			.productType(product.getProductType().name())
			.amount(product.getAmount().doubleValue())
			.months(product.getMonths())
			.interestRate(product.getInterestRate().doubleValue())
			.interestType(product.getInterestType().name())
			.taxType(product.getTaxType().name())
			.taxRate(product.getTaxRate().doubleValue())
			.build();
	}
}
