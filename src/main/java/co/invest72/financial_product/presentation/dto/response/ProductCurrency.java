package co.invest72.financial_product.presentation.dto.response;

import co.invest72.money.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class ProductCurrency {
	private String code;
	private String unit;

	public static ProductCurrency from(Currency currency) {
		return new ProductCurrency(currency.getCode(), currency.getUnit());
	}
}
