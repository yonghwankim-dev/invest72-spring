package co.invest72.exchange_rate.domain;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;

public interface ExchangeRateRepository {
	void save(ExchangeRate exchangeRate);
}
