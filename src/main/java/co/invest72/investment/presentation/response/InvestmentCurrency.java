package co.invest72.investment.presentation.response;

import co.invest72.money.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class InvestmentCurrency {
	private String code;
	private String unit;
	private String name;

	public static InvestmentCurrency from(Currency currency) {
		return new InvestmentCurrency(currency.getCode(), currency.getUnit(), currency.getName());
	}
}
