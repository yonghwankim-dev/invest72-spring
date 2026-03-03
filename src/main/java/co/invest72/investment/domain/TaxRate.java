package co.invest72.investment.domain;

import java.math.BigDecimal;

public interface TaxRate {
	int applyTo(int amount);

	BigDecimal applyTo(BigDecimal amount);

	double getValue();
}
