package co.invest72.exchange_rate.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Primary
@Profile("!test")
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
}
