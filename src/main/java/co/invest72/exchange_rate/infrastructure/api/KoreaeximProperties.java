package co.invest72.exchange_rate.infrastructure.api;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external-api.koreaexim")
public class KoreaeximProperties {
	private final String apiKey;
	private final String baseUrl;

	public KoreaeximProperties(String apiKey, String baseUrl) {
		this.apiKey = Objects.requireNonNull(apiKey);
		this.baseUrl = Objects.requireNonNull(baseUrl);
	}
}
