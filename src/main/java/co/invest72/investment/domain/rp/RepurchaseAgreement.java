package co.invest72.investment.domain.rp;

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
}
