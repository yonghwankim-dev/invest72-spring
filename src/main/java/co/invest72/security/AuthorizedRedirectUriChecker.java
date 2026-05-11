package co.invest72.security;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizedRedirectUriChecker {

	private final List<String> allowedOrigins;

	public AuthorizedRedirectUriChecker(List<String> allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	public boolean check(String uri) {
		return createURI(uri)
			.filter(this::checkUri)
			.isPresent();
	}
	
	private Optional<URI> createURI(String uri) {
		try {
			URI clientRedirectUri = URI.create(uri);
			return Optional.of(clientRedirectUri);
		} catch (NullPointerException | IllegalArgumentException e) {
			log.warn("invalid uri : {}, uri= {}", e.getMessage(), uri);
			return Optional.empty();
		}
	}

	private boolean checkUri(URI clientURI) {
		return allowedOrigins.stream()
			.anyMatch(authorizedRedirectUri ->
				createURI(authorizedRedirectUri)
					.filter(authURI -> authURI.getScheme().equals(clientURI.getScheme()))
					.filter(authURI -> authURI.getHost().equalsIgnoreCase(clientURI.getHost()))
					.filter(authURI -> authURI.getPort() == clientURI.getPort())
					.isPresent()
			);
	}
}
