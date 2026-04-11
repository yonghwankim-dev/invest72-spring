package co.invest72.money.domain;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateProvider {
	Optional<BigDecimal> getRate(Currency from, Currency to);

	void addRate(Pair pair, BigDecimal rate);

	void clear();
}
