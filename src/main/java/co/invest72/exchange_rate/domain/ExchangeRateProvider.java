package co.invest72.exchange_rate.domain;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateProvider {
	Optional<BigDecimal> getRate(Currency from, Currency to);
}
