package co.invest72.financial_product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ProductMonths {
	@Column(name = "months", nullable = false)
	private Integer value;

	public ProductMonths(Integer value) {
		this.value = value;
		validate(this.value);
	}

	private void validate(Integer value) {
		if (value == null || value < 0) {
			throw new IllegalArgumentException("기간은 0개월 이상이어야 합니다.");
		}
		if (value > 11988) {
			throw new IllegalArgumentException("기간은 11988개월(999년) 초과할 수 없습니다.");
		}
	}
}
