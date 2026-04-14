package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;
import java.util.function.Function;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

class KoreaeximClientTest {

	private KoreaeximClient client;

	@BeforeEach
	void setUp() {
		WebClient webClient = BDDMockito.mock(WebClient.class, Answers.RETURNS_DEEP_STUBS);
		WebClient.ResponseSpec responseSpec = BDDMockito.mock(WebClient.ResponseSpec.class);
		BDDMockito.given(
				webClient.get().uri(ArgumentMatchers.anyString(), ArgumentMatchers.any(Function.class)).retrieve())
			.willReturn(responseSpec);
		BDDMockito.given(responseSpec.bodyToFlux(ExchangeJsonResponse.class))
			.willReturn(Flux.empty());
		Flux<ExchangeJsonResponse> flux = Flux.just(new ExchangeJsonResponse(1, "KRW", "1"));
		BDDMockito.given(responseSpec.bodyToFlux(ExchangeJsonResponse.class))
			.willReturn(flux);

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
