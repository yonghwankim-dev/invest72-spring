package co.invest72.exchange_rate.domain;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository {
	void save(Currency currency);

	Optional<Currency> findByCode(String code);

	List<Currency> findAll();
}
