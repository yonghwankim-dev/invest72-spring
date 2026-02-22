package co.invest72.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import lombok.Getter;

@ConfigurationProperties(prefix = "app.cors")
@Getter
public class CorsConfigurationProperties {
	private final List<String> allowedOrigin;
	private final List<String> allowedMethods;
	private final List<String> allowedHeaders;
	private final Boolean allowCredentials;

	@ConstructorBinding
	public CorsConfigurationProperties(List<String> allowedOrigin, List<String> allowedMethods,
		List<String> allowedHeaders,
		Boolean allowCredentials) {
		this.allowedOrigin = allowedOrigin;
		this.allowedMethods = allowedMethods;
		this.allowedHeaders = allowedHeaders;
		this.allowCredentials = allowCredentials;
	}
}
