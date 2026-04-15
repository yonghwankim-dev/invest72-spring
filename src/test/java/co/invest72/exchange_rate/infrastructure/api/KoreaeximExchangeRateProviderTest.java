package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import co.invest72.exchange_rate.domain.Currency;
import co.invest72.exchange_rate.domain.ExchangeRateProvider;
import reactor.core.publisher.Flux;

class KoreaeximExchangeRateProviderTest {

	private ExchangeRateProvider provider;
	private KoreaeximClient client;

	@BeforeEach
	void setUp() {
		client = BDDMockito.mock(KoreaeximClient.class);
		ExchangeJsonResponse response1 = new ExchangeJsonResponse(1, "KRW", "1");
		ExchangeJsonResponse response2 = new ExchangeJsonResponse(1, "USD", "1,000");
		BDDMockito.given(client.exchangeJson())
			.willReturn(Flux.just(response1, response2));
		provider = new KoreaeximExchangeRateProvider(client);
	}

	@DisplayName("환율 업데이트")
	@Test
	void updateRates() {
		// when
		provider.updateRates().blockLast();
		// then
		Assertions.assertThat(provider.getRate(Currency.won(), Currency.dollar()).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(0.001));
		Assertions.assertThat(provider.getRate(Currency.dollar(), Currency.won()).orElseThrow())
			.isEqualByComparingTo(BigDecimal.valueOf(1000));
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
		Assertions.assertThat(provider.getRate(Currency.won(), Currency.dollar())).isEmpty();
		Assertions.assertThat(provider.getRate(Currency.dollar(), Currency.won())).isEmpty();
	}

	@DisplayName("환율 업데이트 - 특정 응답의 result이 0이면 해당 환율을 업데이트하지 않아야 한다")
	@Test
	void updateRates_whenResultIsZero_thenNotUpdateExchangeRate() {
		// given
		BDDMockito.given(client.exchangeJson())
			.willReturn(Flux.just(new ExchangeJsonResponse(0, "USD", "1,000")));
		// when
		provider.updateRates().blockLast();
		// then
		Assertions.assertThat(provider.getRate(Currency.won(), Currency.dollar())).isEmpty();
		Assertions.assertThat(provider.getRate(Currency.dollar(), Currency.won())).isEmpty();
	}
}
