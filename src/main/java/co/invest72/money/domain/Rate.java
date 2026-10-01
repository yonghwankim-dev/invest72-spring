package co.invest72.money.domain;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;

@Getter
public class Rate implements Comparable<Rate> {

	private final BigDecimal value;

	private Rate(BigDecimal value) {
		this.value = Objects.requireNonNull(value, "Rate value must not be null");
	}

	public static Rate of(BigDecimal value) {
		return new Rate(value);
	}

	/**
	 * 소수점 비율을 퍼센트 형태의 BigDecimal로 반
	 * @return {@link BigDecimal} 퍼센트 값
	 */
	public BigDecimal toPercentage() {
		return this.value.multiply(BigDecimal.valueOf(100));
	}

	public Money applyTo(Money target) {
		Objects.requireNonNull(target, "Money must not be null");
		return target.times(this.value);
	}

	@Override
	public int compareTo(Rate other) {
		return this.value.compareTo(other.value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Rate rate))
			return false;
		return this.compareTo(rate) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return "Rate{" + "value=" + value + '}';
	}
}
