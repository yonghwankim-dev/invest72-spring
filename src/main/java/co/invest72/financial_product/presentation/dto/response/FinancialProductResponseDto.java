package co.invest72.financial_product.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FinancialProductResponseDto {

	private String id;
	private String userId;
	private String name;
	private String investmentType;
	private BigDecimal amount;
	private Integer months;
	private BigDecimal interestRate;
	private String interestType;
	private String taxType;
	private BigDecimal taxRate;
	private LocalDate startDate;
	private LocalDateTime createdAt;
}
