package co.invest72.exchange_rate.infrastructure.api;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

public class KoreaeximClient {
	private final WebClient webClient;
	private final KoreaeximProperties properties;

	public KoreaeximClient(WebClient webClient, KoreaeximProperties properties) {
		this.webClient = webClient;
		this.properties = properties;
	}

	public Flux<ExchangeJsonResponse> exchangeJson() {
		return this.webClient.get()
			.uri(properties.getExchangeJson(), uriBuilder -> uriBuilder
				.queryParam("authkey", properties.getApiKey())
				.queryParam("data", "AP01")
				.build())
			.retrieve()
			.bodyToFlux(ExchangeJsonResponse.class);
	}
}
