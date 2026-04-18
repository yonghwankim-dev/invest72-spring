package co.invest72.exchange_rate.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.entity.ExchangeRate;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.api.ExchangeJsonResponse;
import co.invest72.money.domain.Currency;

@Service
public class ExchangeRateUpdateHandler {
	private final ExchangeRateService exchangeRateService;
	private final ExchangeRateRepository exchangeRateRepository;
	private final ApplicationEventPublisher eventPublisher;

	public ExchangeRateUpdateHandler(ExchangeRateService exchangeRateService,
		ExchangeRateRepository exchangeRateRepository, ApplicationEventPublisher eventPublisher) {
		this.exchangeRateService = Objects.requireNonNull(exchangeRateService);
		this.exchangeRateRepository = Objects.requireNonNull(exchangeRateRepository);
		this.eventPublisher = Objects.requireNonNull(eventPublisher);
	}

	@Transactional
	public void handleUpdateRates(ExchangeJsonResponse response) {
		// 1. 값 정규화
		BigDecimal rate = parseRate(response);
		Currency from = extractCurrency(response);
		Currency to = Currency.won();

		// 2. 캐시 업데이트 (도메인 모델 기반)
		eventPublisher.publishEvent(new ExchangeRateUpdateEvent(from, to, rate));

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
			rate = rate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_EVEN);
		}
		return rate;
	}

	@TransactionalEventListener
	private void cacheExchangeRate(ExchangeRateUpdateEvent event) {
		Currency from = event.getFrom();
		Currency to = event.getTo();
		BigDecimal rate = event.getRate();
		exchangeRateService.saveRate(from, to, rate);
	}
}
