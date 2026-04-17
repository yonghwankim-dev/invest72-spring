package co.invest72.exchange_rate.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.money.domain.Pair;

public class ExchangeRateService {
	private final Map<Pair, BigDecimal> exchangeRateCache = new ConcurrentHashMap<>();

	public void saveRate(Currency from, Currency to, BigDecimal rate) {
		// 정방향 저장
		exchangeRateCache.put(new Pair(from, to), rate);

		// 역방향 저장
		if (isPositiveRate(rate)) {
			BigDecimal reverseRate = BigDecimal.ONE.divide(rate, 4, RoundingMode.HALF_EVEN);
			exchangeRateCache.put(new Pair(to, from), reverseRate);
		}
	}

	private boolean isPositiveRate(BigDecimal rate) {
		return rate.compareTo(BigDecimal.ZERO) > 0;
	}

	public Optional<BigDecimal> getRate(Pair key) {
		if (key.isSameCurrency()) {
			return Optional.of(BigDecimal.ONE);
		}
		return Optional.ofNullable(exchangeRateCache.get(key));
	}
}
