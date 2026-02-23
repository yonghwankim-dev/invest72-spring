package co.invest72.financial_product.presentation.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	private LocalDate startDate;
	private LocalDateTime createdAt;
}
