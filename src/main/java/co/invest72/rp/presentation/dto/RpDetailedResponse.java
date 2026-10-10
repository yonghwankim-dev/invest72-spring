package co.invest72.rp.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class RpDetailedResponse {
	private final String id; // 식별자
	private final String investmentType; // 상품 타입
	private final String name; // 상품 이름
	private final BigDecimal amount; // 매수금액
	private final String currency; // 통화 코드
	private final BigDecimal interestRate; // 연이자율
	private final LocalDate startDate; // 시작 금액
	private final Integer termOfAgreement; // 약정 일수(days)
	private final BigDecimal maturityInterest; // 만기 시 받는 이자 금액
	private final BigDecimal currentInterest; // 현재 이자 금액
	private final BigDecimal currentInterestRate; // 현재 이자 수익율
	private final Boolean isAutoReinvest; // 만기시 자동 재투자 여부

	@Builder
	public RpDetailedResponse(
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
}
