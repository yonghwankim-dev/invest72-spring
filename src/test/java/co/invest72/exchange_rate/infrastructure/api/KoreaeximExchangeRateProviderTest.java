package co.invest72.exchange_rate.infrastructure.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import co.invest72.exchange_rate.application.ExchangeRateUpdateHandler;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import co.invest72.exchange_rate.domain.KoreaeximClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class KoreaeximExchangeRateProviderTest {

	private ExchangeRateProvider provider;
	private KoreaeximClient client;
	private ExchangeRateUpdateHandler exchangeRateUpdateHandler;

	@BeforeEach
	void setUp() {
		client = BDDMockito.mock(KoreaeximClient.class);
		exchangeRateUpdateHandler = BDDMockito.mock(ExchangeRateUpdateHandler.class);
		provider = new KoreaeximExchangeRateProvider(client, exchangeRateUpdateHandler);
	}

	@DisplayName("환율 업데이트 - 응답 결과가 KRW, USD 2개인 경우 통화를 업데이트한다")
	@Test
	void should_update_rates_when_response_contains_krw_usd() {
		// given
		ExchangeJsonResponse response1 = new ExchangeJsonResponse(1, "KRW", "1", "한국 원");
		ExchangeJsonResponse response2 = new ExchangeJsonResponse(1, "USD", "1,000", "미국 달러");
		BDDMockito.given(client.exchangeJson())
			.willReturn(Flux.just(response1, response2));
		// when & then
		StepVerifier.create(provider.updateRates())
			.expectNext(response1, response2)
			.verifyComplete();

		BDDMockito.verify(exchangeRateUpdateHandler, Mockito.times(1))
			.handleUpdateRates(response1);
		BDDMockito.verify(exchangeRateUpdateHandler, Mockito.times(1))
			.handleUpdateRates(response2);
	}

	@DisplayName("환율 업데이트 - 응답한 데이터가 비어있으면 저장되지 않는다")
	@Test
	void should_not_saved_rates_when_flux_is_empty() {
		// given
		BDDMockito.given(client.exchangeJson())
			.willReturn(Flux.empty());
		// when & then
		StepVerifier.create(provider.updateRates())
			.verifyComplete();
	}

	@DisplayName("환율 업데이트 - 특정 응답의 result 필드가 0이면 해당 환율을 업데이트하지 않아야 한다")
	@Test
	void should_not_update_rates_when_result_is_zero() {
		// given
		ExchangeJsonResponse response = new ExchangeJsonResponse(0, "USD", "1,000", "미국 달러");
		BDDMockito.given(client.exchangeJson())
			.willReturn(Flux.just(response));
		// when & then
		StepVerifier.create(provider.updateRates())
			.expectNextCount(0)
			.verifyComplete();
		// then
		BDDMockito.then(exchangeRateUpdateHandler).shouldHaveNoInteractions();
	}
}
