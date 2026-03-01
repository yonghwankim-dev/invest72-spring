package co.invest72.investment.domain.investment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentDay {
	@Column(name = "payment_day")
	private int value;

	public PaymentDay(int value) {
		this.value = value;
		if (this.value < 1 || this.value > 31) {
			throw new IllegalArgumentException("납입일은 1일부터 31일까지 가능합니다.");
		}
	}
}
