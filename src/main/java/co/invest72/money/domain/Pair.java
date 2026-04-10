package co.invest72.money.domain;

import java.util.Objects;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Pair {
	private final Currency from;
	private final Currency to;

	public Pair(Currency from, Currency to) {
		this.from = Objects.requireNonNull(from);
		this.to = Objects.requireNonNull(to);
	}
}
