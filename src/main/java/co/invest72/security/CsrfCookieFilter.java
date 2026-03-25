package co.invest72.security;

import java.io.IOException;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * CSRF 토큰을 발급하고, 쿠키에 저장하는 필터입니다.
 * 해당 필터는 GET 요청시에도 CSRF 토큰이 쿠키에 저장되도록 보장합니다.
 */
@Slf4j
public class CsrfCookieFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		if (csrfToken != null) {
			// 토큰을 명시적으로 get하는 시점에 CookieCsrfTokenRepository가 쿠키를 생성 및 저장합니다.
			csrfToken.getToken();
		}
		filterChain.doFilter(request, response);
	}
}
