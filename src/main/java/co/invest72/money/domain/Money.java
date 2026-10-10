package co.invest72.money.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import jakarta.annotation.Nonnull;
import lombok.Getter;

@Getter
public class Money implements Comparable<Money> {

	private static final int SCALE = 19;

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
		BigDecimal rounded = this.value.divide(divisor, SCALE, RoundingMode.HALF_EVEN);
		return of(rounded, this.currency);
	}

	/**
	 * 금액을 금액으로 나누어 비율(수익율/이자율) 반환 (Money / Money = BigDecimal)
	 * <p>
	 * - 대상 금액 대비 현재 금액의 비율을 계산한다
	 * @param target {@link Money} 기준이 되는 금액 (분모)
	 * @return {@link BigDecimal} 비율 (예: 0.05=5%), target이 0이거나 통화가 서로 다른 경우 0을 반환한다
	 * @throws NullPointerException target이 null인 경우 예외를 발생시킨다.
	 * @throws IllegalArgumentException 통화가 서로 다르면 예외를 발생시킨다.
	 */
	public BigDecimal ratioOf(Money target) {
		Objects.requireNonNull(target, "target must not null");
		if (target.isZero()) {
			return BigDecimal.ZERO;
		}
		if (!this.currency.equals(target.currency)) {
			throw new IllegalArgumentException(
				"currencies are different, this.currency=" + this.currency + ", target.currency=" + target.currency);
		}
		return this.value.divide(target.value, SCALE, RoundingMode.HALF_EVEN);
	}

	public Money reduce(Currency target, BigDecimal rate) {
		BigDecimal amount = value.multiply(rate);
		return Money.of(amount, target);
	}

	/**
	 * Money 객체가 값을 스스로 절사/반올림하여 반환한다.
	 *
	 * @return {@link Money}
	 */
	public Money round() {
		BigDecimal rounded = this.value.setScale(0, RoundingMode.HALF_EVEN);
		return of(rounded, this.currency);
	}

	private boolean isZero() {
		return BigDecimal.ZERO.compareTo(this.value) == 0;
	}

	private boolean isZero(BigDecimal divisor) {
		return BigDecimal.ZERO.compareTo(divisor) == 0;
	}

	public boolean isNegative() {
		return this.value.compareTo(BigDecimal.ZERO) < 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Money other))
			return false;
		// 통화가 다르면 false 반환
		if (!Objects.equals(this.currency, other.currency)) {
			return false;
		}
		// BigDecimal의 scale 차이를 무시하고 수치적 가치를 비교
		return this.value.compareTo(other.value) == 0;
	}

	@Override
	public int hashCode() {
		// compareTo 결과와 일치하도록 stripTrailingZeros 후 hashCode 생성
		return Objects.hash(value.stripTrailingZeros(), currency);
	}

	@Override
	public int compareTo(@Nonnull Money other) {
		validate(other);
		return this.value.compareTo(other.value);
	}

	@Override
	public String toString() {
		// 표현(UI/출력)용으로만 소수점 2자리 정제 적용
		BigDecimal displayValue = this.value.setScale(2, RoundingMode.HALF_EVEN);
		return displayValue.toPlainString() + " " + currency.getCode();
	}
}
