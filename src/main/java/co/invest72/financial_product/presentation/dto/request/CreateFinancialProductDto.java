package co.invest72.financial_product.presentation.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CreateFinancialProductDto {
	private String name;
	private String productType;
	private Long amount;
	private Integer months;
	private Double interestRate;
	private String interestType;
	private String taxType;
	private Double taxRate;
	private LocalDate startDate;
}
