package co.invest72.rp.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import co.invest72.rp.entity.RepurchaseAgreementEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RpDetailedResponse {
	private String id; // 식별자
	private String investmentType; // 상품 타입
	private String name; // 상품 이름
	private BigDecimal amount; // 매수금액
	private String currency; // 통화 코드
	private BigDecimal interestRate; // 연이자율
	private LocalDate startDate; // 시작 금액
	private BigDecimal currentInterest; // 현재 이자 금액
	private BigDecimal currentInterestRate; // 현재 이자 수익율
	private Boolean isAutoReinvest; // 만기시 자동 재투자 여부

	public static RpDetailedResponse fromEntity(RepurchaseAgreementEntity rp) {
		return RpDetailedResponse.builder()
			.id(rp.getId())
			.investmentType(rp.getTypeName())
			.name(rp.getName())
			.amount(rp.getAmount().getValue())
			.currency(rp.getAmount().getCurrency())
			.interestRate(rp.getProductAnnualInterestRate().getValue())
			.startDate(rp.getStartDate())
			.currentInterest(BigDecimal.ZERO)
			.currentInterestRate(BigDecimal.ZERO)
			.isAutoReinvest(true)
			.build();
	}
}
