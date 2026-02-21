package co.invest72.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		// 1. 요청 헤더에서 JWT 추출
		String jwt = resolveToken(request);
		// 2. 토큰 유효성 검사
		if (Strings.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			// 3. 토큰에서 유저 식별자(UUID) 및 권한 정보 추출
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			log.debug("Authenticated user: {}", authentication.getName());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			log.debug("No valid JWT token found in request");
		}
		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (Strings.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
