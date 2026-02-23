package co.invest72.financial_product.presentation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.common.validation.FinancialAmount;
import co.invest72.common.validation.FinancialMonths;
import co.invest72.common.validation.FinancialProductName;
import co.invest72.common.validation.FinancialRate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CreateFinancialProductDto {
	@FinancialProductName
	private String name;

	@NotBlank(message = "상품 유형은 필수입니다.")
	private String productType;

	@FinancialAmount
	private BigDecimal amount;

	@FinancialMonths
	private Integer months;

	@FinancialRate
	private BigDecimal interestRate;

	@NotBlank(message = "이자 유형은 필수입니다.")
	private String interestType;

	@NotBlank(message = "세율 유형은 필수입니다.")
	private String taxType;

	@FinancialRate
	private BigDecimal taxRate;

	@NotNull(message = "시작 날짜는 필수입니다.")
	private LocalDate startDate;
}
