package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.money.domain.Pair;

public class FixedExchangeRateProvider implements ExchangeRateProvider {
	// Key: 대상 통화, Value: 1단위당 원화 금액
	private final Map<Pair, BigDecimal> rates = new ConcurrentHashMap<>();

	public FixedExchangeRateProvider() {
		rates.put(new Pair(Currency.won(), Currency.dollar()), BigDecimal.valueOf(0.001));
		rates.put(new Pair(Currency.dollar(), Currency.won()), BigDecimal.valueOf(1000));
	}

	@Override
	public Optional<BigDecimal> getRate(Currency from, Currency to) {
		if (from.equals(to)) {
			return Optional.of(BigDecimal.ONE);
		}
		Pair pair = new Pair(from, to);
		return Optional.ofNullable(rates.get(pair));
	}
}
