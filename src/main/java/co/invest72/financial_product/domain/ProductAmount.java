package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAmount {

	private static final BigDecimal MAX_AMOUNT = new BigDecimal("99999999999999999"); // 99999조

	@Column(name = "amount", nullable = false, precision = 19, scale = 2)
	private BigDecimal value;

	@Column(name = "currency", nullable = false, length = 3)
	private String currency;

	private ProductAmount(BigDecimal value, Currency currency) {
		Objects.requireNonNull(value, "금액은 null일 수 없습니다.");
		Objects.requireNonNull(currency, "통화는 null일 수 없습니다.");
		validateRange(value);
		this.value = value;
		this.currency = currency.getCode();
	}

	private void validateRange(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
		}
		if (value.compareTo(MAX_AMOUNT) > 0) {
			throw new IllegalArgumentException("금액은 99999조원을 초과할 수 없습니다.");
		}
	}

	public static ProductAmount won(BigDecimal amount) {
		return from(Money.won(amount));
	}

	public static ProductAmount dollar(BigDecimal amount) {
		return from(Money.dollar(amount));
	}

	public static ProductAmount from(Money money) {
		Objects.requireNonNull(money, "Money 객체는 null일 수 없습니다.");
		return new ProductAmount(money.getValue(), money.getCurrency());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductAmount that = (ProductAmount)o;
		return this.value.compareTo(that.value) == 0 && Objects.equals(this.currency, that.currency);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value.stripTrailingZeros(), currency);
	}
}
