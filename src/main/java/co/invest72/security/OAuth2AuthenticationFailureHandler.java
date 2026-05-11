package co.invest72.security;

import static co.invest72.security.HttpCookieOAuth2AuthorizationRequestRepository.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private static final String LOGIN_URL = "/login";
	private final AuthorizedRedirectUriChecker checker;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		log.warn("Failed Authentication : {}", exception.getMessage(), exception);

		// 세션에 저장해둔 redirect_uri 가져오기
		String targetUrl = (String)request.getSession().getAttribute(REDIRECT_URI_PARAM_COOKIE_NAME);

		// 화이트리스트 검증 로직 추가
		if (targetUrl != null && !checker.check(targetUrl)) {
			log.warn("비인가 리다이렉트 시도 차단: {}", targetUrl);
			targetUrl = null; // 안전하지 않은 주소일 경우 백엔드 기본 로그인으로 강제 설정
			log.debug("targetUrl : {}", targetUrl);
		}

		// 메시지 인코딩
		String message = (exception.getLocalizedMessage() != null)
			? exception.getLocalizedMessage()
			: "Failed Authentication";
		String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

		if (targetUrl == null || targetUrl.isBlank()) {
			// 저장된 주소가 없다면 기본 백엔드 로그인 페이지로
			targetUrl = LOGIN_URL;
		} else {
			targetUrl = targetUrl + LOGIN_URL;
		}

		// 에러 파라미터 결합 (기존 파라미터 유무 체크)
		String redirectUrl = targetUrl.contains("?")
			? targetUrl + "&error=" + encodedMessage
			: targetUrl + "?error=" + encodedMessage;

		// 사용한 세션 데이터 삭제 (Clean up)
		request.getSession().removeAttribute(REDIRECT_URI_PARAM_COOKIE_NAME);

		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}
}
