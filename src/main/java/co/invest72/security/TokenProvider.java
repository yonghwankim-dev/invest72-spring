package co.invest72.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenProvider {
	private final Key key;

	/**
	 * 생성자에서 JWT 서명에 사용할 비밀 키와 토큰 유효 기간을 설정합니다.
	 * @param secretKey JWT 서명에 사용할 비밀 키 (Base64로 인코딩된 문자열)
	 */
	public TokenProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * JWT 토큰의 유효성을 검증합니다. 토큰이 올바르게 서명되었는지, 만료되지 않았는지 등을 확인합니다.
	 * @param token 검증할 JWT 토큰 문자열
	 * @return 토큰이 유효하면 true, 그렇지 않으면 false를 반환합니다.
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			log.warn("Invalid JWT token: {}", e.getMessage());
		}
		return false;
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		// 1. 권한 정보 추출
		Object auth = claims.get("auth");
		if (auth == null) {
			auth = "";
		}

		Collection<? extends GrantedAuthority> authorities = Arrays.stream(auth.toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.toList();

		// 2. PrincipalUser 객체 생성 (여기서는 사용자 이름을 UUID로 사용)
		PrincipalUser principal = PrincipalUser.create(claims.getSubject());

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}
}
