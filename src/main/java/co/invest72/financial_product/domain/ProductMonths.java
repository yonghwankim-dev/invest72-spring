package co.invest72.financial_product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
		if (value > 9999) {
			throw new IllegalArgumentException("기간은 9999개월을 초과할 수 없습니다.");
		}
	}
}
