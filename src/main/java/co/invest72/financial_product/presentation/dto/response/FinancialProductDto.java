package co.invest72.financial_product.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class FinancialProductDto {
	private final String id;
	private final String userId;
	private final String name;
	private final String investmentType;
	private final BigDecimal amount;
	private final Integer months;
	private final BigDecimal interestRate;
	private final String interestType;
	private final String taxType;
	private final BigDecimal taxRate;
	private final LocalDate startDate;
	private final LocalDateTime createdAt;
}
