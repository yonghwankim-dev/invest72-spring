package co.invest72.investment.domain;

import java.math.BigDecimal;

public interface Taxable {
	int applyTax(int interest);

	BigDecimal applyTax(BigDecimal interest);

	String getTaxType();

	double getTaxRate();
}
