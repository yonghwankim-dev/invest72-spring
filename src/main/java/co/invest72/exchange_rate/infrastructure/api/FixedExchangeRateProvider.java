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
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FixedExchangeRateProvider implements ExchangeRateProvider {
	private final ExchangeRateService exchangeRateService;
	private final ExchangeRateRepository exchangeRateRepository;

	public FixedExchangeRateProvider(ExchangeRateService exchangeRateService,
		ExchangeRateRepository exchangeRateRepository) {
		this.exchangeRateService = Objects.requireNonNull(exchangeRateService);
		this.exchangeRateRepository = Objects.requireNonNull(exchangeRateRepository);
	}

	@Override
	public Optional<BigDecimal> getRate(Currency from, Currency to) {
		return exchangeRateService.getRate(new Pair(from, to));
	}

	@Override
	public Flux<ExchangeJsonResponse> updateRates() {
		log.info("FixedExchangeRateProvider: 고정 환율 모드이므로 업데이트를 생략합니다.");
		Currency won = Currency.won();
		Currency dollar = Currency.dollar();
		int success = 1;
		return Flux.just(
				new ExchangeJsonResponse(1, won.getCode(), "1", won.getName()),
				new ExchangeJsonResponse(1, dollar.getCode(), "1000", dollar.getName())
			).filter(response -> response.getResult() == success)
			.flatMap(response -> Mono.fromRunnable(() -> handleUpdateRates(response))
				.thenReturn(response));
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
