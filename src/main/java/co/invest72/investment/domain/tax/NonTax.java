package co.invest72.investment.domain.tax;

import java.math.BigDecimal;

import co.invest72.investment.domain.Taxable;
import co.invest72.money.domain.Money;

public class NonTax implements Taxable {
	@Override
	public int applyTax(int interest) {
		return 0;
	}

	@Override
	public BigDecimal applyTax(BigDecimal interest) {
		return BigDecimal.ZERO;
	}

	@Override
	public Money applyTax(Money interest) {
		return interest.times(BigDecimal.ZERO);
	}

	@Override
	public String getTaxType() {
		return TaxType.NON_TAX.getDescription();
	}

	@Override
	public BigDecimal getTaxRate() {
		return BigDecimal.ZERO;
	}
}
