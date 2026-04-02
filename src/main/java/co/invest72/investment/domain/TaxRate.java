package co.invest72.investment.domain;

import java.math.BigDecimal;

import co.invest72.money.domain.Money;

public interface TaxRate {
	int applyTo(int amount);

	BigDecimal applyTo(BigDecimal amount);

	Money applyTo(Money amount);

	BigDecimal getValue();
}
