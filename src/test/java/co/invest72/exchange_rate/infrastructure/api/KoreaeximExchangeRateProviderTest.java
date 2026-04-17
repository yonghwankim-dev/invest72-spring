package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import co.invest72.exchange_rate.application.ExchangeRateUpdateHandler;
import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.exchange_rate.domain.ExchangeRateRepository;
import co.invest72.exchange_rate.domain.service.ExchangeRateService;
import co.invest72.exchange_rate.infrastructure.persistence.InMemoryExchangeRateRepository;
import co.invest72.money.domain.Pair;
import reactor.core.publisher.Flux;

class KoreaeximExchangeRateProviderTest {

	private ExchangeRateProvider provider;
	private RealKoreaeximClient client;
	private ExchangeRateService exchangeRateService;

	@BeforeEach
	void setUp() {
		client = BDDMockito.mock(RealKoreaeximClient.class);
		ExchangeJsonResponse response1 = new ExchangeJsonResponse(1, "KRW", "1", "한국 원");
		ExchangeJsonResponse response2 = new ExchangeJsonResponse(1, "USD", "1,000", "미국 달러");
		BDDMockito.given(client.exchangeJson())
			.willReturn(Flux.just(response1, response2));
		ExchangeRateRepository exchangeRateRepository = new InMemoryExchangeRateRepository();
		exchangeRateService = new ExchangeRateService();
		ExchangeRateUpdateHandler exchangeRateUpdateHandler = new ExchangeRateUpdateHandler(exchangeRateService,
			exchangeRateRepository);
		provider = new KoreaeximExchangeRateProvider(client, exchangeRateUpdateHandler);
	}

	@DisplayName("환율 업데이트")
	@Test
	void updateRates() {
		// when
		provider.updateRates().blockLast();
		// then
		Assertions.assertThat(exchangeRateService.getRate(new Pair(Currency.won(), Currency.dollar())).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(0.001));
		Assertions.assertThat(exchangeRateService.getRate(new Pair(Currency.dollar(), Currency.won())).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(1000));
	}

	@DisplayName("환율 업데이트 - JPY(100)과 같이 100단위로 오는 경우 환율에는 1단위로 정규화 시켜 저장해야 한다")
	@Test
	void updateRates_whenCurrencyUnitContain100_thenOneNormalization() {
		// given
		ExchangeJsonResponse response1 = new ExchangeJsonResponse(1, "KRW", "1", "한국 원");
		ExchangeJsonResponse response2 = new ExchangeJsonResponse(1, "JPY(100)", "951.05", "미국 달러");
		BDDMockito.given(client.exchangeJson())
			.willReturn(Flux.just(response1, response2));
		// when
		provider.updateRates().blockLast();
		// then
		Assertions.assertThat(exchangeRateService.getRate(new Pair(Currency.won(), Currency.from("JPY"))).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(0.1051));
		Assertions.assertThat(exchangeRateService.getRate(new Pair(Currency.from("JPY"), Currency.won())).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(9.5105));
	}

	@DisplayName("환율 업데이트 - 응답한 데이터가 비어있으면 저장되지 않는다")
	@Test
	void updateRates_whenEmptyFlux_thenReturnEmptyOptional() {
		// given
		BDDMockito.given(client.exchangeJson())
			.willReturn(Flux.empty());
		// when
		provider.updateRates().blockLast();
		// then
		Assertions.assertThat(exchangeRateService.getRate(new Pair(Currency.won(), Currency.dollar()))).isEmpty();
		Assertions.assertThat(exchangeRateService.getRate(new Pair(Currency.dollar(), Currency.won()))).isEmpty();
	}

	@DisplayName("환율 업데이트 - 특정 응답의 result이 0이면 해당 환율을 업데이트하지 않아야 한다")
	@Test
	void updateRates_whenResultIsZero_thenNotUpdateExchangeRate() {
		// given
		BDDMockito.given(client.exchangeJson())
			.willReturn(Flux.just(new ExchangeJsonResponse(0, "USD", "1,000", "미국 달러")));
		// when
		provider.updateRates().blockLast();
		// then
		Assertions.assertThat(exchangeRateService.getRate(new Pair(Currency.won(), Currency.dollar()))).isEmpty();
		Assertions.assertThat(exchangeRateService.getRate(new Pair(Currency.dollar(), Currency.won()))).isEmpty();
	}
}
