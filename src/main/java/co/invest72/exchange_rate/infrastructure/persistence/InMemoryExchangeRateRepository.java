package co.invest72.exchange_rate.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.exchange_rate.domain.ExchangeRate;
import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.Pair;

public class InMemoryExchangeRateRepository implements ExchangeRateRepository {

	private final Map<Pair, BigDecimal> store = new ConcurrentHashMap<>();

	public InMemoryExchangeRateRepository() {
		store.put(new Pair(Currency.won(), Currency.dollar()), BigDecimal.valueOf(0.001));
		store.put(new Pair(Currency.dollar(), Currency.won()), BigDecimal.valueOf(1000));
	}

	@Override
	public void save(ExchangeRate exchangeRate) {
	}
}
