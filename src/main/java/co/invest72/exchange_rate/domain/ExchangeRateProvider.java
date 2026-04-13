package co.invest72.exchange_rate.domain;

import java.math.BigDecimal;
import java.util.Optional;

import co.invest72.money.domain.Currency;

public interface ExchangeRateProvider {
	Optional<BigDecimal> getRate(Currency from, Currency to);
}
