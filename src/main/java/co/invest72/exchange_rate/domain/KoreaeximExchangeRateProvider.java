package co.invest72.exchange_rate.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.exchange_rate.infrastructure.api.KoreaeximClient;
import co.invest72.money.domain.Pair;

public class KoreaeximExchangeRateProvider implements ExchangeRateProvider {
	private final KoreaeximClient client;

	// 최신 환율 정보를 메모리에 캐싱(Key: 통화코드, Value: 1단위당 원화 가치)
	private final Map<Pair, BigDecimal> exchangeRateCache;

	public KoreaeximExchangeRateProvider(KoreaeximClient client) {
		this.client = client;
		this.exchangeRateCache = new ConcurrentHashMap<>();
	}

	@Override
	public Optional<BigDecimal> getRate(Currency from, Currency to) {
		if (from.equals(to)) {
			return Optional.of(BigDecimal.ONE);
		}
		Pair pair = new Pair(from, to);
		return Optional.ofNullable(exchangeRateCache.get(pair));
	}
}
