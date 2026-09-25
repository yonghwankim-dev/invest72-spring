package co.invest72.rp.domain;

import java.time.LocalDate;

import co.invest72.money.domain.Money;

/**
 * 환매조건부채권(RP)
 */
public interface RepurchaseAgreement {

	LocalDate calculateExpirationDate(int daysToAdd);

	/**
	 * 약정 일수까지의 이자 금액 계산
	 * <p>
	 * - 이자금액 = 투자 금액 x 약정수익률(연이율) x (예치 일수 / 365)
	 * </p>
	 * @param days 약정 일수
	 * @return 이자 금액
	 */
	Money calculateInterestForDays(int days);

	LocalDate getExpirationDate();
	
	/**
	 * 만기 이자 금액 계산하여 반환
	 * <p>
	 * - 원금 x 연 이자율 x (투자 일수 / 365)
	 * <p>
	 * - 만기 이자 금액 반환시 정수 형태로 반올림하여 반환
	 * @return 만기 시 이자 금액
	 */
	Money calculateMaturityInterest();
}
