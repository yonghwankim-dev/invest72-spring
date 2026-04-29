package co.invest72.exchange_rate.infrastructure.api;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@ConfigurationProperties(prefix = "external-api.koreaexim")
@Getter
public class KoreaeximProperties {
	private final String apiKey;
	private final String baseUri;
	private final String exchangeJson;

	public KoreaeximProperties(String apiKey, String baseUri, String exchangeJson) {
		this.apiKey = Objects.requireNonNull(apiKey);
		this.baseUri = Objects.requireNonNull(baseUri);
		this.exchangeJson = Objects.requireNonNull(exchangeJson);
	}
}
