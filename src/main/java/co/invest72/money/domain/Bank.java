package co.invest72.money.domain;

import java.math.BigDecimal;

public final class Bank {

	private final ExchangeRateProvider exchangeRateProvider;

	public Bank(ExchangeRateProvider exchangeRateProvider) {
		this.exchangeRateProvider = exchangeRateProvider;
	}

	/**
	 * target에 해당하는 통화로 환전하여 반환한다
	 * @param source 대상 금액
	 * @param target 환전하고자 하는 통화 단위
	 * @return 환전된 금액
	 */
	public Money reduce(Money source, Currency target) {
		if (source.getCurrency().equals(target)) {
			return Money.of(source.getValue(), target);
		}

		BigDecimal rate = exchangeRateProvider.getRate(source.getCurrency(), target);
		if (rate == null) {
			throw new IllegalArgumentException("undefined rate, " + source.getCurrency() + "->" + target);
		}
		BigDecimal amount = source.getValue().multiply(rate);
		return Money.of(amount, target);
	}

	public void addRate(Currency from, Currency to, BigDecimal rate) {
		exchangeRateProvider.addRate(new Pair(from, to), rate);
	}

	public void clearRates() {
		exchangeRateProvider.clear();
	}
}
