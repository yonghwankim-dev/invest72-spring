package co.invest72.money.domain;

import java.math.BigDecimal;

public interface ExchangeRateProvider {
	BigDecimal getRate(Currency from, Currency to);

	void addRate(Pair pair, BigDecimal rate);

	void clear();
}
