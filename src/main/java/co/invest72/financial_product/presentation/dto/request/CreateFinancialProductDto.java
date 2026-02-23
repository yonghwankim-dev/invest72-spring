package co.invest72.financial_product.presentation.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CreateFinancialProductDto {
	@NotBlank(message = "상품 이름은 필수입니다.")
	private String name;
	@NotBlank(message = "상품 유형은 필수입니다.")
	private String productType;
	@NotNull(message = "투자 금액은 필수입니다.")
	private Long amount;
	@NotNull(message = "투자 기간은 필수입니다.")
	private Integer months;
	@NotNull(message = "이자율은 필수입니다.")
	private Double interestRate;
	@NotBlank(message = "이자 유형은 필수입니다.")
	private String interestType;
	@NotBlank(message = "세율 유형은 필수입니다.")
	private String taxType;
	@NotNull(message = "세율은 필수입니다.")
	private Double taxRate;
	@NotNull(message = "시작 날짜는 필수입니다.")
	private LocalDate startDate;
}
