package co.invest72.financial_product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import co.invest72.money.domain.Currency;
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

	/**
	 * 금액을 나타내는 객체를 생성한다. 금액은 0원 이상 99999조원 이하이어야 한다.
	 *
	 * @param value 금액
	 * @throws IllegalArgumentException 유효하지 않은 금액인 경우
	 */
	public ProductAmount(BigDecimal value) {
		this(value, Currency.won());
	}

	public ProductAmount(BigDecimal value, Currency currency) {
		validate(value);
		validateCurrency(currency);
		this.value = value;
		this.currency = currency.getCode();
	}

	private void validate(BigDecimal value) {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
		}
		if (value.compareTo(MAX_AMOUNT) > 0) {
			throw new IllegalArgumentException("금액은 99999조원을 초과할 수 없습니다.");
		}
	}

	private void validateCurrency(Currency currency) {
		if (currency == null) {
			throw new IllegalArgumentException("통화는 null이거나 빈 문자열일 수 없습니다.");
		}
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
