package co.invest72.investment.domain;

import java.math.BigDecimal;

import co.invest72.money.domain.Money;

public interface Taxable {
	int applyTax(int interest);

	BigDecimal applyTax(BigDecimal interest);

	Money applyTax(Money interest);

	String getTaxType();

	BigDecimal getTaxRate();
}
