package co.invest72.financial_product.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductMonths {
	private static final int MAX_MONTHS = 999 * 12; // 999년

	@Column(name = "months", nullable = false)
	private Integer value;

	public ProductMonths(Integer value) {
		this.value = Objects.requireNonNull(value, "기간은 null일 수 없습니다.");
		validate(this.value);
	}

	private void validate(Integer value) {
		if (value < 0) {
			throw new IllegalArgumentException("기간은 0개월 이상이어야 합니다.");
		}
		if (value > MAX_MONTHS) {
			throw new IllegalArgumentException("기간은 " + MAX_MONTHS + "개월을 초과할 수 없습니다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductMonths that = (ProductMonths)o;
		return value.compareTo(that.value) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
