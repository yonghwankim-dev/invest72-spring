package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.money.domain.Pair;
import reactor.core.publisher.Flux;

public class KoreaeximExchangeRateProvider implements ExchangeRateProvider {
	private final KoreaeximClient client;

	// 최신 환율 정보를 메모리에 캐싱(Key: 통화코드, Value: 환율)
	private final Map<Pair, BigDecimal> exchangeRateCache;
	private final ExchangeRateRepository exchangeRateRepository;

	public KoreaeximExchangeRateProvider(KoreaeximClient client, ExchangeRateRepository exchangeRateRepository) {
		this.client = Objects.requireNonNull(client);
		this.exchangeRateCache = new ConcurrentHashMap<>();
		this.exchangeRateRepository = Objects.requireNonNull(exchangeRateRepository);
	}

	@Override
	public Optional<BigDecimal> getRate(Currency from, Currency to) {
		if (from.equals(to)) {
			return Optional.of(BigDecimal.ONE);
		}
		Pair pair = new Pair(from, to);
		return Optional.ofNullable(exchangeRateCache.get(pair));
	}

	@Override
	public Flux<ExchangeJsonResponse> updateRates() {
		int success = 1;
		return client.exchangeJson()
			.filter(response -> response.getResult() == success)
			.doOnNext(response -> {
				// response.dealingBaseRate = 1외화 단위당 원화(KRW) 가치
				// ex: currencyUnit="USD", dealingBaseRate="1000"

				// 단위 변환(예: JPY(100), IDR(100) 등 100단위로 오는 경우 1단위로 정규화
				BigDecimal rate = parseRate(response, response.getDealingBaseRate());

				// 통화 코드 추출
				String currencyCode = extractCurrencyCode(response.getCode());
				// 저장소에 없으면 API 정보를 바탕으로 새로 생성하여 저장
				ExchangeRate fromExchangeRate = exchangeRateRepository.findByCode(currencyCode)
					.orElseGet(() -> {
						Currency newCurrency = Currency.of(currencyCode, response.getName());
						ExchangeRate exchangeRate = new ExchangeRate(newCurrency.getCode(), newCurrency.getName(),
							rate);
						exchangeRateRepository.save(exchangeRate);
						return exchangeRate;
					});
				Currency from = Currency.of(fromExchangeRate.getCurrencyCode(), fromExchangeRate.getCurrencyName());
				Currency to = Currency.won();

				// 캐시 업데이트
				exchangeRateCache.put(new Pair(from, to), rate);

				// 역방향 환율 업데이트
				BigDecimal reverseRate = BigDecimal.ONE.divide(rate, 4, RoundingMode.HALF_EVEN);
				exchangeRateCache.put(new Pair(to, from), reverseRate);
			});
	}

	private String extractCurrencyCode(String currencyUnit) {
		return currencyUnit.substring(0, 3);
	}

	private BigDecimal parseRate(ExchangeJsonResponse response, BigDecimal rate) {
		if (response.getCode().contains("(100)")) {
			rate = rate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN);
		}
		return rate;
	}
}
