package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;

import org.springframework.web.reactive.function.client.WebClient;

public class KoreaeximClient {
	private final WebClient webClient;
	private final KoreaeximProperties properties;

	public KoreaeximClient(WebClient webClient, KoreaeximProperties properties) {
		this.webClient = webClient;
		this.properties = properties;
	}

	public BigDecimal exchangeJson() {
		this.webClient.get()
			.uri(properties.getExchangeJson(), uriBuilder -> uriBuilder
				.queryParam("authkey", properties.getApiKey())
				.queryParam("data", "AP01")
				.build())
			.retrieve()
			.bodyToFlux(ExchangeJsonResponse.class)
			.subscribe(System.out::println);
		return BigDecimal.ONE;
	}
}
