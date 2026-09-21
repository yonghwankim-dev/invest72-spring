package co.invest72.rp.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import co.invest72.rp.entity.RepurchaseAgreementEntity;
import lombok.Builder;
import lombok.Getter;

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
	private Integer termOfAgreement; // 약정 일수(days)
	private BigDecimal maturityInterest; // 만기 시 받는 이자 금액
	private BigDecimal currentInterest; // 현재 이자 금액
	private BigDecimal currentInterestRate; // 현재 이자 수익율
	private Boolean isAutoReinvest; // 만기시 자동 재투자 여부

	private RpDetailedResponse(
		String id,
		String investmentType,
		String name,
		BigDecimal amount,
		String currency,
		BigDecimal interestRate,
		LocalDate startDate,
		Integer termOfAgreement,
		BigDecimal maturityInterest,
		BigDecimal currentInterest,
		BigDecimal currentInterestRate,
		Boolean isAutoReinvest
	) {
		this.id = Objects.requireNonNull(id);
		this.investmentType = Objects.requireNonNull(investmentType);
		this.name = Objects.requireNonNull(name);
		this.amount = Objects.requireNonNull(amount);
		this.currency = Objects.requireNonNull(currency);
		this.interestRate = Objects.requireNonNull(interestRate);
		this.startDate = Objects.requireNonNull(startDate);
		this.termOfAgreement = Objects.requireNonNull(termOfAgreement);
		this.maturityInterest = Objects.requireNonNull(maturityInterest);
		this.currentInterest = Objects.requireNonNull(currentInterest);
		this.currentInterestRate = Objects.requireNonNull(currentInterestRate);
		this.isAutoReinvest = Objects.requireNonNull(isAutoReinvest);
	}

	public static RpDetailedResponse fromEntity(RepurchaseAgreementEntity rp) {
		return RpDetailedResponse.builder()
			.id(rp.getId())
			.investmentType(rp.getTypeName())
			.name(rp.getName())
			.amount(rp.getAmount().getValue())
			.currency(rp.getAmount().getCurrency())
			.interestRate(rp.getProductAnnualInterestRate().getValue())
			.startDate(rp.getStartDate())
			.termOfAgreement(rp.getDays())
			.maturityInterest(BigDecimal.ZERO)
			.currentInterest(BigDecimal.ZERO)
			.currentInterestRate(BigDecimal.ZERO)
			.isAutoReinvest(true)
			.build();
	}
}
