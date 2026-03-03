package co.invest72.investment.domain.tax;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

import co.invest72.investment.domain.TaxRate;

public class FixedTaxRate implements TaxRate {

	private final BigDecimal rate;

	public FixedTaxRate(double rate) {
		this(BigDecimal.valueOf(rate));
	}

	public FixedTaxRate(BigDecimal rate) {
		this.rate = rate;
		validate();
	}

	private void validate() {
		if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(BigDecimal.ONE) >= 0) {
			throw new IllegalArgumentException("Tax rate must be between 0 and 1 (exclusive).");
		}
	}

	@Override
	public int applyTo(int amount) {
		return rate
			.multiply(BigDecimal.valueOf(amount), MathContext.DECIMAL64)
			.setScale(0, RoundingMode.HALF_EVEN)
			.intValueExact();
	}

	@Override
	public BigDecimal applyTo(BigDecimal amount) {
		return rate
			.multiply(amount, MathContext.DECIMAL64);
	}

	@Override
	public double getRate() {
		return rate.doubleValue();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;
		FixedTaxRate that = (FixedTaxRate)object;
		return rate.compareTo(that.rate) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rate);
	}
}
