package co.invest72.exchange_rate.infrastructure.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.exchange_rate.domain.ExchangeRate;
import co.invest72.exchange_rate.domain.ExchangeRateRepository;

public class InMemoryExchangeRateRepository implements ExchangeRateRepository {

	private final Map<String, ExchangeRate> store = new ConcurrentHashMap<>();

	@Override
	public void save(ExchangeRate exchangeRate) {
		store.put(exchangeRate.getCurrencyCode(), exchangeRate);
	}
}
