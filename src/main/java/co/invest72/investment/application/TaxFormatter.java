package co.invest72.investment.application;

import java.math.BigDecimal;

public interface TaxFormatter {
	String format(double value);

	String format(BigDecimal value);
}
