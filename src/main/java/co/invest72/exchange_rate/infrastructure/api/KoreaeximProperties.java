package co.invest72.exchange_rate.infrastructure.api;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external-api.koreaexim")
public class KoreaeximProperties {
	private final String apiKey;
	private final String baseUri;

	public KoreaeximProperties(String apiKey, String baseUri) {
		this.apiKey = Objects.requireNonNull(apiKey);
		this.baseUri = Objects.requireNonNull(baseUri);
	}
}
