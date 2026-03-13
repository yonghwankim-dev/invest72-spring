package co.invest72.money.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		validateCurrency(this.currency);
	}

	private void validate(BigDecimal value) {
		if (value == null) {
			throw new IllegalArgumentException("금액은 null일 수 없습니다.");
		}
	}

	private void validateCurrency(Currency currency) {
		if (currency == null) {
			throw new IllegalArgumentException("통화는 null일 수 없습니다.");
		}
	}

	public static Money dollar(int value) {
		return dollar(BigDecimal.valueOf(value));
	}

	public static Money dollar(BigDecimal value) {
		return new Money(value, Currency.dollar());
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

	public Money times(int multiplier) {
		return times(BigDecimal.valueOf(multiplier));
	}

	public Money times(BigDecimal multiplier) {
		return new Money(this.value.multiply(multiplier), this.currency);
	}

	private void validate(Money money) {
		if (money == null) {
			throw new IllegalArgumentException("Money 객체는 null일 수 없습니다.");
		}
		if (!this.currency.equals(money.currency)) {
			throw new IllegalArgumentException("통화가 일치하지 않습니다.");
		}
	}

	public Money divide(BigDecimal divisor) {
		BigDecimal newValue = this.value.divide(divisor, 2, RoundingMode.HALF_EVEN);
		return new Money(newValue, currency);
	}

	public boolean isNegative() {
		return this.value.compareTo(BigDecimal.ZERO) < 0;
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
		return compareToValue(money) == 0 && this.currency.equals(money.currency);
	}

	private int compareToValue(Money other) {
		return this.value.setScale(2, RoundingMode.HALF_EVEN)
			.compareTo(other.value.setScale(2, RoundingMode.HALF_EVEN));
	}

	@Override
	public int hashCode() {
		return Objects.hash(value.stripTrailingZeros(), currency);
	}

	@Override
	public String toString() {
		return value.stripTrailingZeros() + currency.getCode();
	}
}
