package co.invest72.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import lombok.Getter;

@ConfigurationProperties(prefix = "app.cors")
@Getter
public class CorsConfigurationProperties {
	private final List<String> allowedOrigins;
	private final List<String> allowedMethods;
	private final List<String> allowedHeaders;
	private final Boolean allowCredentials;

	@ConstructorBinding
	public CorsConfigurationProperties(List<String> allowedOrigins, List<String> allowedMethods,
		List<String> allowedHeaders,
		Boolean allowCredentials) {
		this.allowedOrigins = allowedOrigins != null ? allowedOrigins : List.of();
		this.allowedMethods = allowedMethods != null ? allowedMethods : List.of();
		this.allowedHeaders = allowedHeaders != null ? allowedHeaders : List.of();
		this.allowCredentials = allowCredentials != null ? allowCredentials : Boolean.FALSE;
	}
}
