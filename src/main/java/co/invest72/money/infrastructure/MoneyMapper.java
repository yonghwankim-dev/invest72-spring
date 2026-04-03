package co.invest72.money.infrastructure;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import co.invest72.money.domain.Money;

@Component
public class MoneyMapper {

	public BigDecimal toBigDecimal(Money money) {
		return money.getValue();
	}
}
