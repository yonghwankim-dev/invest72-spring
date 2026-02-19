package co.invest72.investment.domain.tax;

import java.math.BigDecimal;

import co.invest72.investment.domain.TaxRate;
import co.invest72.investment.domain.Taxable;

public class StandardTax implements Taxable {

	private final TaxRate taxRate;

	public StandardTax(TaxRate taxRate) {
		this.taxRate = taxRate;
	}

	@Override
	public int applyTax(int interest) {
		return taxRate.applyTo(interest);
	}

	@Override
	public BigDecimal applyTax(BigDecimal interest) {
		return taxRate.applyTo(interest);
	}

	@Override
	public String getTaxType() {
		return TaxType.STANDARD.getDescription();
	}

	@Override
	public double getTaxRate() {
		return taxRate.getRate();
	}
}
