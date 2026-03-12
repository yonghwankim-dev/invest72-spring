package co.invest72.money.domain;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class Money implements Comparable<Money> {

	private final BigDecimal value;
	private final Currency currency;

	private Money(BigDecimal value, Currency currency) {
		this.value = value;
		this.currency = currency;
		validate(this.value);
	}

	private void validate(BigDecimal value) {
		if (value == null) {
			throw new IllegalArgumentException("금액은 null일 수 없습니다.");
		}
	}

	public static Money dollar(int value) {
		return new Money(BigDecimal.valueOf(value), Currency.dollar());
	}

	public static Money won(int value) {
		return won(BigDecimal.valueOf(value));
	}

	public static Money won(BigDecimal value) {
		return new Money(value, Currency.won());
	}

	public static Money of(BigDecimal value, String currency) {
		return new Money(value, Currency.of(currency));
	}

	public Money add(Money addend) {
		validate(addend);
		return new Money(this.value.add(addend.value), this.currency);
	}

	private void validate(Money money) {
		if (money == null) {
			throw new IllegalArgumentException("Money 객체는 null일 수 없습니다.");
		}
		if (!this.currency.equals(money.currency)) {
			throw new IllegalArgumentException("통화가 일치하지 않습니다.");
		}
	}

	@Override
	public int compareTo(@Nonnull Money other) {
		validate(other);
		return this.value.compareTo(other.value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Money money = (Money)o;
		return this.value.compareTo(money.value) == 0 && this.currency.equals(money.currency);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value.stripTrailingZeros(), currency);
	}
}
