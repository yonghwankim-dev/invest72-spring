package co.invest72.exchange_rate.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
	private final JpaExchangeRateRepository jpaRepository;

	@Override
	public void save(ExchangeRate exchangeRate) {
		jpaRepository.save(exchangeRate);
	}

	@Override
	public Optional<ExchangeRate> findByCode(String code) {
		return jpaRepository.findById(code);
	}

	@Override
	public List<ExchangeRate> findAll() {
		return jpaRepository.findAll();
	}

	@Override
	public void clear() {
		jpaRepository.deleteAll();
	}
}
