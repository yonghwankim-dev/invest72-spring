package co.invest72.security;

import java.net.URI;
import java.util.List;

public class AuthorizedRedirectUriChecker {

	private final List<String> allowedOrigins;

	public AuthorizedRedirectUriChecker(List<String> allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	public boolean check(String uri) {
		URI clientRedirectUri = URI.create(uri);
		return allowedOrigins.stream()
			.anyMatch(authorizedRedirectUri -> {
				URI authorizedURI = URI.create(authorizedRedirectUri);
				return
					authorizedURI.getScheme().equalsIgnoreCase(clientRedirectUri.getScheme())
						&& authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
						&& authorizedURI.getPort() == clientRedirectUri.getPort();
			});
	}
}
