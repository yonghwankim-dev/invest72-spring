package co.invest72.financial_product.presentation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CreateFinancialProductDto {
	@NotBlank(message = "상품 이름은 필수입니다.")
	@Size(min = 1, max = 100, message = "상품 이름은 최대 100자까지 입력 가능합니다.")
	private String name;

	@NotBlank(message = "상품 유형은 필수입니다.")
	private String productType;

	@NotNull(message = "투자 금액은 필수입니다.")
	@DecimalMin(value = "0", message = "투자 금액은 0원 이상이어야 합니다.")
	@DecimalMax(value = "10000000000000", message = "투자 금액은 10조원 이하이어야 합니다.")
	private BigDecimal amount;

	@NotNull(message = "투자 기간은 필수입니다.")
	@Min(value = 0, message = "투자 기간은 최소 0개월 이상이어야 합니다.")
	@Max(value = 9999, message = "투자 기간은 최대 9999개월 이하이어야 합니다.")
	private Integer months;

	@NotNull(message = "이자율은 필수입니다.")
	@DecimalMin(value = "0", message = "이자율은 0% 이상이어야 합니다.")
	@DecimalMax(value = "9.9999", message = "이자율은 최대 999.99%까지 설정 가능합니다.")
	private BigDecimal interestRate;

	@NotBlank(message = "이자 유형은 필수입니다.")
	private String interestType;

	@NotBlank(message = "세율 유형은 필수입니다.")
	private String taxType;

	@NotNull(message = "세율은 필수입니다.")
	@DecimalMin(value = "0", message = "세율은 0% 이상이어야 합니다.")
	@DecimalMax(value = "9.9999", message = "세율은 최대 999.99%까지 설정 가능합니다.")
	private BigDecimal taxRate;

	@NotNull(message = "시작 날짜는 필수입니다.")
	private LocalDate startDate;
}
