package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.web.reactive.function.client.WebClient;

class KoreaeximClientTest {

	private KoreaeximClient client;

	@BeforeEach
	void setUp() {
		WebClient webClient = BDDMockito.mock(WebClient.class);
		KoreaeximProperties properties = new KoreaeximProperties("test-api-key", "http://localhost:8080",
			"/exchangeJson");
		client = new KoreaeximClient(webClient, properties);
	}

	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		Assertions.assertThat(client).isNotNull();
	}

	@DisplayName("환율 조회")
	@Test
	void exchangeJson() {
		BigDecimal actual = client.exchangeJson();

		Assertions.assertThat(actual).isEqualTo(BigDecimal.ONE);
	}
}
