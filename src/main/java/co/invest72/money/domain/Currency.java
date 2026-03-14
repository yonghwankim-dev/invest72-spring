package co.invest72.money.domain;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class Currency {
	private final String code;

	private Currency(String code) {
		this.code = code;
	}

	public static Currency of(String code) {
		return new Currency(code);
	}

	public static Currency dollar() {
		return new Currency("USD");
	}

	public static Currency won() {
		return new Currency("KRW");
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
}
