package co.invest72.financial_product.presentation.dto.response;

import java.math.BigDecimal;
import java.util.Objects;

import co.invest72.money.domain.Currency;
import lombok.Getter;

@Getter
public class TotalBalance {
	private final BigDecimal amount;
	private final ProductCurrency currency;

	public TotalBalance(BigDecimal amount, Currency currency) {
		this.amount = Objects.requireNonNull(amount);
		this.currency = Objects.requireNonNull(ProductCurrency.from(currency));
	}
}
