package co.invest72.security;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirectUri";
	private static final String OAUTH2_AUTH_REQUEST_NAME = "oauth2_auth_request";

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return (OAuth2AuthorizationRequest)request.getSession().getAttribute(OAUTH2_AUTH_REQUEST_NAME);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
		HttpServletResponse response) {
		if (authorizationRequest == null) {
			request.getSession().removeAttribute(OAUTH2_AUTH_REQUEST_NAME);
			request.getSession().removeAttribute(REDIRECT_URI_PARAM_COOKIE_NAME);
			return;
		}

		// 세션에 인증 요청 정보 저장
		request.getSession().setAttribute(OAUTH2_AUTH_REQUEST_NAME, authorizationRequest);

		// 쿼리 파라미터로 넘어온 redirect_url을 세션에 저장
		String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
		if (redirectUriAfterLogin != null && !redirectUriAfterLogin.isBlank()) {
			request.getSession().setAttribute(REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		return this.loadAuthorizationRequest(request);
	}
}
