package co.invest72.exchange_rate.infrastructure.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import co.invest72.exchange_rate.domain.ExchangeRateProvider;

class KoreaeximExchangeRateProviderTest {
	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		// given
		KoreaeximClient client = BDDMockito.mock(KoreaeximClient.class);
		// when
		ExchangeRateProvider provider = new KoreaeximExchangeRateProvider(client);
		// then
		Assertions.assertThat(provider).isNotNull();
	}
}
