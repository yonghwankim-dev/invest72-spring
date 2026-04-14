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

	@BeforeEach
	void setUp() {
		KoreaeximClient client = BDDMockito.mock(KoreaeximClient.class);
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

}
