package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.money.domain.Pair;
import reactor.core.publisher.Flux;

public class KoreaeximExchangeRateProvider implements ExchangeRateProvider {
	private final KoreaeximClient client;
	private final ExchangeRateRepository exchangeRateRepository;
	private final ExchangeRateService exchangeRateService;

	public KoreaeximExchangeRateProvider(KoreaeximClient client, ExchangeRateRepository exchangeRateRepository,
		ExchangeRateService exchangeRateService) {
		this.client = Objects.requireNonNull(client);
		this.exchangeRateRepository = Objects.requireNonNull(exchangeRateRepository);
		this.exchangeRateService = Objects.requireNonNull(exchangeRateService);
	}

	@Override
	public Optional<BigDecimal> getRate(Currency from, Currency to) {
		if (from.equals(to)) {
			return Optional.of(BigDecimal.ONE);
		}
		Pair pair = new Pair(from, to);
		return exchangeRateService.getRate(pair);
	}

	@Override
	public Flux<ExchangeJsonResponse> updateRates() {
		int success = 1;
		return client.exchangeJson()
			.filter(response -> response.getResult() == success)
			.doOnNext(this::handleUpdateRates);
	}

	private void handleUpdateRates(ExchangeJsonResponse response) {
		// 1. 값 정규화
		BigDecimal rate = parseRate(response);
		Currency from = extractCurrency(response);
		Currency to = Currency.won();

		// 2. 캐시 업데이트 (도메인 모델 기반)
		exchangeRateService.saveRate(from, to, rate);

		// 3. DB 업데이트 (Persistence 모델)
		ExchangeRate exchangeRate = new ExchangeRate(from.getCode(), from.getName(), rate);
		exchangeRateRepository.save(exchangeRate);
	}

	private Currency extractCurrency(ExchangeJsonResponse response) {
		int start = 0;
		int end = 3;
		String currencyCode = response.getCode().substring(start, end);
		String currencyName = response.getName();
		return Currency.of(currencyCode, currencyName);
	}

	// 단위 변환(예: JPY(100), IDR(100) 등 100단위로 오는 경우 1단위로 정규화
	private BigDecimal parseRate(ExchangeJsonResponse response) {
		BigDecimal rate = response.getDealingBaseRate();
		if (response.getCode().contains("(100)")) {
			rate = rate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN);
		}
		return rate;
	}
}
