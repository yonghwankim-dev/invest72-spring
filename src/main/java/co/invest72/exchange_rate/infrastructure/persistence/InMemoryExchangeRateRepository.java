package co.invest72.exchange_rate.infrastructure.persistence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.money.domain.Currency;

@Repository
@Profile("test")
public class InMemoryExchangeRateRepository implements ExchangeRateRepository {

	private final Map<String, ExchangeRate> store = new ConcurrentHashMap<>();

	public InMemoryExchangeRateRepository() {
		Currency dollar = Currency.dollar();
		save(new ExchangeRate(dollar.getCode(), dollar.getName(), BigDecimal.valueOf(1000)));
		Currency won = Currency.won();
		save(new ExchangeRate(won.getCode(), won.getName(), BigDecimal.valueOf(1)));
	}

	@Override
	public void save(ExchangeRate exchangeRate) {
		store.put(exchangeRate.getCurrencyCode(), exchangeRate);
	}

	@Override
	public Optional<ExchangeRate> findByCode(String code) {
		return Optional.ofNullable(store.get(code.toUpperCase()));
	}

	@Override
	public List<ExchangeRate> findAll() {
		return new ArrayList<>(store.values());
	}

	@Override
	public void clear() {
		store.clear();
	}
}
