package co.invest72.investment.domain.tax;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

import co.invest72.investment.domain.TaxRate;

public class FixedTaxRate implements TaxRate {

	private final BigDecimal value;

	public FixedTaxRate(double value) {
		this(BigDecimal.valueOf(value));
	}

	public FixedTaxRate(BigDecimal value) {
		this.value = value;
		validate();
	}

	private void validate() {
		if (value == null || value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(BigDecimal.ONE) >= 0) {
			throw new IllegalArgumentException("Tax rate must be between 0 and 1 (exclusive).");
		}
	}

	@Override
	public int applyTo(int amount) {
		return value
			.multiply(BigDecimal.valueOf(amount), MathContext.DECIMAL64)
			.setScale(0, RoundingMode.HALF_EVEN)
			.intValueExact();
	}

	@Override
	public BigDecimal applyTo(BigDecimal amount) {
		return value
			.multiply(amount, MathContext.DECIMAL64);
	}

	@Override
	public BigDecimal getValue() {
		return value;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		FixedTaxRate that = (FixedTaxRate)object;
		return value.compareTo(that.value) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
