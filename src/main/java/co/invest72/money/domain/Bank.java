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
		BigDecimal rate = BigDecimal.valueOf(0.001);
		BigDecimal amount = source.getValue().multiply(rate);
		return Money.of(amount, target);
	}
}
