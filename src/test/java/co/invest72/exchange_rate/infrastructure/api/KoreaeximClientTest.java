package co.invest72.exchange_rate.infrastructure.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KoreaeximClientTest {

	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		KoreaeximProperties properties = new KoreaeximProperties("test-api-key", "http://localhost:8080",
			"/exchangeJson");

		KoreaeximClient client = new KoreaeximClient(properties);

		Assertions.assertThat(client).isNotNull();
	}
}
