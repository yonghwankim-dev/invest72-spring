package co.invest72.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final String redirectUri;

	public OAuth2AuthenticationSuccessHandler(
		@Value("${app.oauth2.authorized-redirect-uri}") String redirectUri) {
		super("/");
		this.redirectUri = redirectUri;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		// redirectUri는 프론트에서 로그인 성공 후 리다이렉트할 URL입니다. 예를 들어, "http://localhost:3000/login-success"와 같은 URL이 될 수 있습니다.
		String targetUrl = UriComponentsBuilder.fromUriString(redirectUri).build().toUriString();
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
