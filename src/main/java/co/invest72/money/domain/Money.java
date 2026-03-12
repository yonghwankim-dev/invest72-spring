package co.invest72.money.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

	private final BigDecimal value;
	private final String currency;

	private Money(BigDecimal value, String currency) {
		this.value = value;
		this.currency = currency;
	}

	public static Money dollar(int value) {
		return new Money(BigDecimal.valueOf(value), "USD");
	}

	public static Money won(int value) {
		return new Money(BigDecimal.valueOf(value), "KRW");
	}

	public Money add(Money addend) {
		validate(addend);
		return new Money(this.value.add(addend.value), this.currency);
	}

	private void validate(Money money) {
		if (!this.currency.equalsIgnoreCase(money.currency)) {
			throw new IllegalArgumentException("통화가 일치하지 않습니다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Money money = (Money)o;
		return this.value.compareTo(money.value) == 0 && this.currency.equalsIgnoreCase(money.currency);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, currency);
	}
}
