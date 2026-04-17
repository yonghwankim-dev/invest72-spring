package co.invest72.money.domain;

import java.util.Objects;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CurrencyPair {
	private final Currency from;
	private final Currency to;

	public CurrencyPair(Currency from, Currency to) {
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
	}

	public boolean isSameCurrency() {
		return this.from.equals(to);
	}
}
