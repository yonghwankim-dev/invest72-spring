package co.invest72.investment.domain.investment;

import java.time.LocalDate;

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

	/**
	 * 주어진 날짜가 납입일 또는 그 이후인지 검사하는 메서드
	 * @param date 검사할 날짜
	 * @return 주어진 날짜의 일(day of month)이 납입일과 같거나 이후인 경우 true, 그렇지 않으면 false
	 */
	public boolean isPaidOn(LocalDate date) {
		return date.getDayOfMonth() >= this.value;
	}
}
