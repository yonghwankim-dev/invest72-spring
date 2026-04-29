package co.invest72.financial_product.presentation.dto.response;

import java.math.BigDecimal;
import java.util.Objects;

import co.invest72.money.domain.Money;
import lombok.Getter;

@Getter
public class TotalEstimatedInterest {
	private final BigDecimal amount;
	private final ProductCurrency currency;

	private TotalEstimatedInterest(BigDecimal amount, ProductCurrency currency) {
		this.amount = Objects.requireNonNull(amount);
		this.currency = Objects.requireNonNull(currency);
	}

	public static TotalEstimatedInterest from(Money amount) {
		return new TotalEstimatedInterest(amount.getValue(), ProductCurrency.from(amount.getCurrency()));
	}
}
