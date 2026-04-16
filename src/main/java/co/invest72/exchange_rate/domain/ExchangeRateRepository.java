package co.invest72.exchange_rate.domain;

import java.util.List;
import java.util.Optional;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;

public interface ExchangeRateRepository {
	void save(ExchangeRate exchangeRate);

	Optional<ExchangeRate> findByCode(String code);

	List<ExchangeRate> findAll();
}
