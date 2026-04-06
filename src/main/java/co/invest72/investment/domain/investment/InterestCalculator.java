package co.invest72.investment.domain.investment;

import co.invest72.investment.domain.InterestRate;
import co.invest72.money.domain.Money;

public interface InterestCalculator {
	/**
	 * 이자 금액을 계산하여 반환한다.
	 *
	 * @param originalPrincipal 초기 원금 (단리 계산용)
	 * @param currentPrincipal 현재 시점의 원리금 (복리 계산용)
	 * @param interestRate 이율
	 * @return 이자금액
	 */
	Money calculate(Money originalPrincipal, Money currentPrincipal, InterestRate interestRate);
}
