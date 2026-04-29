package co.invest72.exchange_rate.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import co.invest72.exchange_rate.domain.entity.ExchangeRate;

public interface JpaExchangeRateRepository extends JpaRepository<ExchangeRate, String> {
	
}
