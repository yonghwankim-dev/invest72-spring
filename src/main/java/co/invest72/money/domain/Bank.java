package co.invest72.money.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import lombok.EqualsAndHashCode;

public final class Bank {

	private static Bank instance;

	private final Map<Pair, BigDecimal> rates = new ConcurrentHashMap<>();

	private Bank() {
		rates.put(new Pair(Currency.won(), Currency.dollar()), BigDecimal.valueOf(0.001));
		rates.put(new Pair(Currency.dollar(), Currency.won()), BigDecimal.valueOf(1000));
	}

	public static Bank getInstance() {
		if (instance == null) {
			instance = new Bank();
		}
		return instance;
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

		BigDecimal rate = rates.get(new Pair(source.getCurrency(), target));
		if (rate == null) {
			throw new IllegalArgumentException("undefined rate, " + source.getCurrency() + "->" + target);
		}
		BigDecimal amount = source.getValue().multiply(rate);
		return Money.of(amount, target);
	}

	public void addRate(Currency from, Currency to, BigDecimal rate) {
		rates.put(new Pair(from, to), rate);
	}

	public void clearRates() {
		rates.clear();
	}

	@EqualsAndHashCode
	private static class Pair {
		private final Currency from;
		private final Currency to;

		public Pair(Currency from, Currency to) {
			this.from = Objects.requireNonNull(from);
			this.to = Objects.requireNonNull(to);
		}
	}
}
