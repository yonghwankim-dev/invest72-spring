package co.invest72.security;

import static co.invest72.security.HttpSessionOAuth2AuthorizationRequestRepository.*;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	public OAuth2AuthenticationSuccessHandler() {
		super("/");
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		// 로그인 성공 시점의 새로운 CSRF 토큰을 강제로 생성 및 로드
		CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		if (csrfToken != null) {
			// 이 호출이 CookieCsrfTokenRepository를 트리거하여 새 Set-Cookie 헤더를 만듭니다.
			csrfToken.getToken();
		}

		String targetUrl = (String)request.getSession().getAttribute(REDIRECT_URI_PARAM_SESSION_NAME);
		if (targetUrl == null || targetUrl.isBlank()) {
			// 백엔드 서버의 루트 경로로 리다이렉트
			targetUrl = getDefaultTargetUrl();
		}
		log.info("success login, targetUrl={}", targetUrl);

		// clean up user session data
		request.getSession().removeAttribute(REDIRECT_URI_PARAM_SESSION_NAME);

		// redirect
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
