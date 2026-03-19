package co.invest72.financial_product.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DetailedFinancialProductResponse {
	private String id;
	private String userId;
	private String name;
	private String investmentType;
	private BigDecimal amount;
	private Integer months;
	private Integer paymentDay;
	private BigDecimal interestRate;
	private String interestType;
	private String taxType;
	private BigDecimal taxRate;
	private LocalDate startDate;
	private LocalDateTime createdAt;
	private LocalDate expirationDate; // 만기일
	private BigDecimal balance; // 현재 잔액
	private BigDecimal progress; // 진행률
	private Long remainingDays; // 남은 일수
	private ProductCurrency productCurrency;
}
