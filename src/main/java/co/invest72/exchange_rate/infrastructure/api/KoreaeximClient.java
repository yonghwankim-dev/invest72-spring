package co.invest72.exchange_rate.infrastructure.api;

import java.time.Duration;

import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
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
			.bodyToFlux(ExchangeJsonResponse.class)
			.timeout(Duration.ofSeconds(10))
			.onErrorResume(e -> {
				log.error("환율 조회 중 오류 발생: {}", e.getMessage());
				return Flux.empty();
			});
	}
}
