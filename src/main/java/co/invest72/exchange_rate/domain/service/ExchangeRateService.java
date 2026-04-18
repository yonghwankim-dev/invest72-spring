package co.invest72.exchange_rate.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.money.domain.Currency;
import co.invest72.money.domain.CurrencyPair;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExchangeRateService {
	private final ExchangeRateRepository repository;

	public ExchangeRateService(ExchangeRateRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void saveRate(ExchangeRate exchangeRate) {
		repository.save(exchangeRate);
	}

	/**
	 * 통화쌍에 대한 환율을 계산하여 반환합니다.
	 * <p>
	 * 예를 들어 1USD=1000KRW이라면<br>
	 * USD -> KRW = 1000<br>
	 * KRW -> USD = 0.001<br>
	 * </p>
	 * @param pair 통화쌍
	 * @return 환율값
	 */
	@Transactional(readOnly = true)
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
