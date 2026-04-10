package co.invest72.financial_product.presentation.dto.response;

import java.math.BigDecimal;
import java.util.Objects;

import co.invest72.money.domain.Money;
import lombok.Getter;

@Getter
public class TotalBalance {
	private final BigDecimal amount;
	private final ProductCurrency currency;

	public TotalBalance(Money amount) {
		this.amount = Objects.requireNonNull(amount.getValue());
		this.currency = Objects.requireNonNull(ProductCurrency.from(amount.getCurrency()));
	}
}
