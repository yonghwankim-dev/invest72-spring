package co.invest72.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final TokenProvider tokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		// 실제 서명된 JWT 생성
		String token = tokenProvider.createToken(authentication);

		// React 앱으로 리다이렉 (쿼리 스트링에 토큰 포함)
		String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect")
			.queryParam("token", token)
			.build().toUriString();
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
