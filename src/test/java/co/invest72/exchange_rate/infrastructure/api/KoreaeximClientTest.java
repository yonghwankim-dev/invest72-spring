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
import reactor.test.StepVerifier;

class KoreaeximClientTest {

	private KoreaeximClient client;

	@SuppressWarnings("unchecked")
	@BeforeEach
	void setUp() {
		WebClient webClient = BDDMockito.mock(WebClient.class, Answers.RETURNS_DEEP_STUBS);
		WebClient.ResponseSpec responseSpec = BDDMockito.mock(WebClient.ResponseSpec.class);
		BDDMockito.given(
				webClient.get()
					.uri(ArgumentMatchers.anyString(), ArgumentMatchers.any(Function.class))
					.retrieve())
			.willReturn(responseSpec);
		BDDMockito.given(responseSpec.bodyToFlux(ExchangeJsonResponse.class))
			.willReturn(Flux.empty());
		Flux<ExchangeJsonResponse> flux = Flux.just(
			new ExchangeJsonResponse(1, "KRW", "1"),
			new ExchangeJsonResponse(1, "USD", "1,066.9"));
		BDDMockito.given(responseSpec.bodyToFlux(ExchangeJsonResponse.class))
			.willReturn(flux);

		KoreaeximProperties properties = new KoreaeximProperties("test-api-key", "http://localhost:8080",
			"/exchangeJson");
		client = new KoreaeximClient(webClient, properties);
	}

	@DisplayName("환율 조회")
	@Test
	void exchangeJson() {
		// when
		Flux<ExchangeJsonResponse> flux = client.exchangeJson();

		// then
		ExchangeJsonResponse response1 = new ExchangeJsonResponse(1, "KRW", "1");
		ExchangeJsonResponse response2 = new ExchangeJsonResponse(1, "USD", "1,066.9");
		StepVerifier.create(flux)
			.expectNext(response1)
			.expectNext(response2)
			.expectComplete()
			.verify();
	}

	@DisplayName("BigDecimal 변환 - 쉼표가 포함된 문자열 금액을 BigDecimal로 변환해야 한다")
	@Test
	void convertToBigDecimal_whenAmountContainComma_thenReturnBigDecimal() {
		// given
		String amount = "1,066.9";
		// when
		BigDecimal bigDecimal = new BigDecimal(amount.replace(",", ""));
		// then
		Assertions.assertThat(bigDecimal).isEqualTo(BigDecimal.valueOf(1066.9));
	}

}
