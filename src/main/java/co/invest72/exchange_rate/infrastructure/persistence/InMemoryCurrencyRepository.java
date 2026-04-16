package co.invest72.exchange_rate.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.CurrencyRepository;

public class InMemoryCurrencyRepository implements CurrencyRepository {

	private final Map<String, Currency> store = new ConcurrentHashMap<>();

	public InMemoryCurrencyRepository() {
		save(Currency.dollar());
		save(Currency.won());
	}

	@Override
	public void save(Currency currency) {
		store.put(currency.getCode().toUpperCase(), currency);
	}

	@Override
	public Optional<Currency> findByCode(String code) {
		return Optional.ofNullable(store.get(code.toUpperCase()));
	}

	@Override
	public List<Currency> findAll() {
		return new ArrayList<>(store.values());
	}
}
