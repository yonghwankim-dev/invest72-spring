package co.invest72.money.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.UnaryOperator;

import co.invest72.exchange_rate.infrastructure.api.FixedExchangeRateProvider;
import jakarta.annotation.Nonnull;
import lombok.Getter;

@Getter
public class Money implements Comparable<Money> {

	private static final UnaryOperator<BigDecimal> roundToTwoDecimalPlaces = money -> money.setScale(2,
		RoundingMode.HALF_EVEN);

	private final BigDecimal value;
	private final Currency currency;

	private Money(BigDecimal value, Currency currency) {
		this.value = Objects.requireNonNull(value, "금액은 null일 수 없습니다.");
		this.currency = Objects.requireNonNull(currency, "통화는 null일 수 없습니다.");
	}

	public static Money dollar(int value) {
		return dollar(BigDecimal.valueOf(value));
	}

	public static Money dollar(BigDecimal value) {
		return of(value, Currency.dollar());
	}

	public static Money won(int value) {
		return won(BigDecimal.valueOf(value));
	}

	public static Money won(BigDecimal value) {
		return of(value, Currency.won());
	}

	public static Money of(BigDecimal value, String currency) {
		return of(value, Currency.from(currency));
	}

	public static Money of(BigDecimal value, Currency currency) {
		return new Money(value, currency);
	}

	public Money add(Money addend) {
		validate(addend);
		return of(this.value.add(addend.value), this.currency);
	}

	public Money subtract(Money subtrahend) {
		validate(subtrahend);
		return of(this.value.subtract(subtrahend.value), this.currency);
	}

	public Money times(int multiplier) {
		return times(BigDecimal.valueOf(multiplier));
	}

	public Money times(long multiplier) {
		return times(BigDecimal.valueOf(multiplier));
	}

	public Money times(BigDecimal multiplier) {
		return Money.of(this.value.multiply(multiplier), this.currency);
	}

	private void validate(Money money) {
		Objects.requireNonNull(money, "Money 객체는 null일 수 없습니다.");
		if (!this.currency.equals(money.currency)) {
			throw new IllegalArgumentException("통화가 일치하지 않습니다.");
		}
	}

	/**
	 * 금액을 분모로 나눈 결과를 반환합니다. 분모가 0인 경우, 0을 반환합니다.
	 * @param divisor 분모
	 * @return 나눗셈 결과를 담은 새로운 Money 객체
	 * @throws NullPointerException divisor가 null인 경우
	 */
	public Money divide(BigDecimal divisor) {
		Objects.requireNonNull(divisor, "분모는 null일 수 없습니다.");
		if (isZero(divisor)) {
			return Money.of(BigDecimal.ZERO, this.currency);
		}
		BigDecimal newValue = this.value.divide(divisor, 2, RoundingMode.HALF_EVEN);
		return of(newValue, this.currency);
	}

	private boolean isZero(BigDecimal divisor) {
		return BigDecimal.ZERO.compareTo(divisor) == 0;
	}

	public boolean isNegative() {
		return this.value.compareTo(BigDecimal.ZERO) < 0;
	}

	public Money reduce(Currency currency) {
		Bank bank = new Bank(new FixedExchangeRateProvider());
		return bank.reduce(this, currency);
	}

	@Override
	public int compareTo(@Nonnull Money other) {
		validate(other);
		BigDecimal roundedThisValue = roundToTwoDecimalPlaces.apply(this.value.stripTrailingZeros());
		BigDecimal roundedOtherValue = roundToTwoDecimalPlaces.apply(other.value.stripTrailingZeros());
		return roundedThisValue.compareTo(roundedOtherValue);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Money other = (Money)o;
		return this.compareTo(other) == 0 && this.currency.equals(other.currency);
	}

	@Override
	public int hashCode() {
		BigDecimal roundedValue = roundToTwoDecimalPlaces.apply(this.value.stripTrailingZeros());
		return Objects.hash(roundedValue, currency);
	}

	@Override
	public String toString() {
		return roundToTwoDecimalPlaces.apply(value.stripTrailingZeros()) + currency.getCode();
	}
}
