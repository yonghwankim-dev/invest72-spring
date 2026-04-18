package co.invest72.exchange_rate.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.transaction.annotation.Transactional;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.CurrencyPair;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExchangeRateService {
	private final Map<CurrencyPair, BigDecimal> exchangeRateCache;
	private final ExchangeRateRepository repository;

	public ExchangeRateService(ExchangeRateRepository repository) {
		this.exchangeRateCache = new ConcurrentHashMap<>();
		this.repository = repository;
	}

	@Transactional
	public void saveRate(ExchangeRate exchangeRate) {
		repository.save(exchangeRate);
	}

	public Optional<BigDecimal> getRate(CurrencyPair pair) {
		if (pair.isSameCurrency()) {
			return Optional.of(BigDecimal.ONE);
		}
		Currency from = pair.getFrom();
		Currency to = pair.getTo();

		ExchangeRate fromExchangeRate;
		ExchangeRate toExchangeRate;
		try {
			fromExchangeRate = repository.findByCode(from.getCode()).orElseThrow();
			toExchangeRate = repository.findByCode(to.getCode()).orElseThrow();
		} catch (NoSuchElementException e) {
			log.warn(e.getMessage());
			return Optional.empty();
		}

		return calRate(fromExchangeRate, toExchangeRate);
	}
	
	private Optional<BigDecimal> calRate(ExchangeRate fromExchangeRate, ExchangeRate toExchangeRate) {
		try {
			final int scale = 10;
			BigDecimal result = fromExchangeRate.getBasicRateOfExchange()
				.divide(toExchangeRate.getBasicRateOfExchange(), scale, RoundingMode.HALF_EVEN);
			return Optional.of(result);
		} catch (ArithmeticException e) {
			log.warn(e.getMessage());
			return Optional.empty();
		}
	}
}
