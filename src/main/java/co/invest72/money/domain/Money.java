package co.invest72.money.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

	private final BigDecimal value;

	private Money(BigDecimal value) {
		this.value = value;
	}

	public static Money dollar(int value) {
		return new Money(BigDecimal.valueOf(value));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Money money = (Money)o;
		return this.value.compareTo(money.value) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
