package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.invest72.exchange_rate.application.ExchangeRateUpdateHandler;
import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.persistence.InMemoryExchangeRateRepository;

class FixedExchangeRateProviderTest {

	private ExchangeRateProvider exchangeRateProvider;

	@BeforeEach
	void setUp() {
		ExchangeRateService exchangeRateService = new ExchangeRateService();
		ExchangeRateRepository exchangeRateRepository = new InMemoryExchangeRateRepository();
		ExchangeRateUpdateHandler exchangeRateUpdateHandler = new ExchangeRateUpdateHandler(exchangeRateService,
			exchangeRateRepository);
		exchangeRateProvider = new FixedExchangeRateProvider(exchangeRateService, exchangeRateUpdateHandler);
		exchangeRateProvider.updateRates().blockLast();
	}

	@DisplayName("환율 조회 - 원화 -> 달러에 대한 환율 조회")
	@Test
	void getRate_whenKRWToUSE_thenReturnRate() {
		// given
		Currency from = Currency.won();
		Currency to = Currency.dollar();
		// when
		BigDecimal rate = exchangeRateProvider.getRate(from, to).orElseThrow();
		// then
		Assertions.assertThat(rate).isEqualByComparingTo(BigDecimal.valueOf(0.001));
	}

	@DisplayName("환율 조회 - 통화가 동일한 경우 1이 반환되어야 한다")
	@Test
	void getRate_whenKRWToKRW_thenReturnOne() {
		// given
		Currency won = Currency.won();
		// when
		BigDecimal rate = exchangeRateProvider.getRate(won, won).orElseThrow();
		// then
		Assertions.assertThat(rate).isEqualTo(BigDecimal.ONE);
	}
}
