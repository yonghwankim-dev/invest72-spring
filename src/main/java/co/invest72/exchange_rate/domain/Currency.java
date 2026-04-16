package co.invest72.exchange_rate.domain;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 통화 단위를 나타내는 클래스입니다. ISO 4217 통화 코드를 사용하여 통화를 표현합니다.
 * 예를 들어, "USD"는 미국 달러, "KRW"는 한국 원화를 나타냅니다.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public final class Currency {
	private final String code;
	private final String symbol;
	private final String name;

	private Currency(String code, String symbol, String name) {
		String normalizedCode = Objects.requireNonNull(code).trim().toUpperCase();
		validate(normalizedCode);
		this.code = normalizedCode;
		this.symbol = Objects.requireNonNull(symbol);
		this.name = Objects.requireNonNull(name);
	}

	private void validate(String code) {
		if (code == null) {
			throw new IllegalArgumentException("통화 코드(code)는 null일 수 없습니다.");
		}

		String normalizedCode = code.trim().toUpperCase();
		if (normalizedCode.isEmpty()) {
			throw new IllegalArgumentException("통화 코드(code)는 빈 문자열일 수 없습니다.");
		}

		if (normalizedCode.length() != 3) {
			throw new IllegalArgumentException("통화 코드(code)는 3자리여야 합니다.");
		}
	}

	public static Currency of(String code, String symbol, String name) {
		return new Currency(code, symbol, name);
	}

	public static Currency from(String code) {
		Objects.requireNonNull(code, "통화 코드(code)는 null이면 안됩니다.");
		String normalizedCode = code.trim().toUpperCase();
		if ("USD".equalsIgnoreCase(normalizedCode)) {
			return dollar();
		} else if ("KRW".equalsIgnoreCase(normalizedCode)) {
			return won();
		}
		return new Currency(normalizedCode, normalizedCode, "Unknown");
	}

	public static Currency dollar() {
		return new Currency("USD", "$", "달러");
	}

	public static Currency won() {
		return new Currency("KRW", "₩", "원화");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Currency currency = (Currency)o;
		return this.code.equalsIgnoreCase(currency.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code.toUpperCase());
	}

	@Override
	public String toString() {
		return code;
	}
}
