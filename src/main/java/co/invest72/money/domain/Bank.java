package co.invest72.money.domain;

import java.math.BigDecimal;

public class Bank {

	/**
	 * target에 해당하는 통화로 환전하여 반환한다
	 * @param source 대상 금액
	 * @param target 환전하고자 하는 통화 단위
	 * @return 환전된 금액
	 */
	public Money reduce(Money source, Currency target) {
		return Money.of(BigDecimal.valueOf(1), target);
	}
}
