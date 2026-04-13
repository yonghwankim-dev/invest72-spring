package co.invest72.exchange_rate.infrastructure.api;

import java.math.BigDecimal;

import org.springframework.web.reactive.function.client.WebClient;

public class KoreaeximClient {
	private final WebClient webClient;
	private final KoreaeximProperties properties;

	public KoreaeximClient(KoreaeximProperties properties) {
		this.webClient = WebClient.builder()
			.baseUrl(properties.getBaseUri())
			.build();
		this.properties = properties;
	}

	public BigDecimal exchangeJson() {
		return null;
	}
}
