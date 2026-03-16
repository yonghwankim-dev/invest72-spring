package co.invest72.security;

import java.io.IOException;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CsrfCookieFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		if (csrfToken != null) {
			// 토큰을 명시적으로 get하는 시점에 CookieCsrfTokenRepository가 쿠키를 생성합니다.
			csrfToken.getToken();
		}
		filterChain.doFilter(request, response);
	}
}
