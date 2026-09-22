package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.money.domain.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "currency_code", nullable = false)
	private ExchangeRate exchangeRate;

	private ProductAmount(BigDecimal value, ExchangeRate exchangeRate) {
		validateRange(value);
		this.value = Objects.requireNonNull(value, "value must not null");
		this.exchangeRate = Objects.requireNonNull(exchangeRate, "exchangeRate must not null");
	}

	private void validateRange(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
		}
		if (value.compareTo(MAX_AMOUNT) > 0) {
			throw new IllegalArgumentException("금액은 99999조원을 초과할 수 없습니다.");
		}
	}

	public static ProductAmount won(BigDecimal amount, ExchangeRate exchangeRate) {
		return from(Money.won(amount), exchangeRate);
	}

	public static ProductAmount dollar(BigDecimal amount, ExchangeRate exchangeRate) {
		return from(Money.dollar(amount), exchangeRate);
	}

	public static ProductAmount from(Money money, ExchangeRate exchangeRate) {
		Objects.requireNonNull(money, "Money 객체는 null일 수 없습니다.");
		return of(money.getValue(), exchangeRate);
	}

	public static ProductAmount of(BigDecimal amount, ExchangeRate exchangeRate) {
		return new ProductAmount(amount, exchangeRate);
	}

	public String getCurrencyCode() {
		return exchangeRate.getCurrencyCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ProductAmount that))
			return false;
		return this.value.compareTo(that.value) == 0 && Objects.equals(exchangeRate, that.exchangeRate);
	}

	@Override
	public int hashCode() {
		BigDecimal normalizedValue = (value != null) ? value.stripTrailingZeros() : null;
		return Objects.hash(normalizedValue, exchangeRate);
	}

	@Override
	public String toString() {
		return "ProductAmount{" +
			"value=" + value +
			", exchangeRate=" + exchangeRate +
			'}';
	}
}
