package co.invest72.financial_product.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import co.invest72.financial_product.domain.FinancialProduct;
import co.invest72.investment.domain.Investment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FinancialProductSummaryResponse {
	private String id; // 식별자
	private String name; // 상품명
	private String investmentType; // 투자 유형
	private BigDecimal interestRate; // 이자율
	private LocalDate startDate; // 시작일
	private LocalDate expirationDate; // 만기일
	private BigDecimal balance; // 현재 잔액(원금)
	private BigDecimal expectedInterest; // 예상 이자
	private BigDecimal progress; // 진행률
	private long remainingDays; // 남은 일수
	private LocalDateTime createAt; // 생성시

	public static FinancialProductSummaryResponse from(
		FinancialProduct product,
		LocalDate today,
		Investment investment
	) {
		return FinancialProductSummaryResponse.builder()
			.id(product.getId())
			.name(product.getName())
			.investmentType(product.getInvestmentType().name())
			.interestRate(product.getInterestRate().getValue())
			.startDate(product.getStartDate())
			.expirationDate(product.getExpirationDate())
			.balance(product.calculateBalance(today)) // 엔티티 내부에서 타입별 계산
			.expectedInterest(BigDecimal.valueOf(investment.getTotalInterest()))
			.progress(product.getProgressByLocalDate(today))
			.remainingDays(product.getRemainingDaysByLocalDate(today))
			.createAt(product.getCreatedAt())
			.build();
	}
}
