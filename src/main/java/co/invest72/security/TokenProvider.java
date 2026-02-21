package co.invest72.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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
	private final long tokenValidityInMilliseconds;

	/**
	 * 생성자에서 JWT 서명에 사용할 비밀 키와 토큰 유효 기간을 설정합니다.
	 * @param secretKey JWT 서명에 사용할 비밀 키 (Base64로 인코딩된 문자열)
	 * @param tokenValidityInMilliseconds 토큰의 유효 기간 (밀리초 단위, 기본값: 36000000밀리초 = 10시간)
	 */
	public TokenProvider(@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.token-validity-in-milli-seconds:36000000}") long tokenValidityInMilliseconds) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
	}

	/**
	 * 인증 정보를 기반으로 JWT 토큰을 생성합니다. 토큰에는 사용자 이름과 권한 정보가 포함됩니다.
	 * @param authentication 인증 객체로부터 사용자 이름과 권한 정보를 추출하여 JWT 토큰을 생성합니다.
	 * @return 생성된 JWT 토큰 문자열
	 */
	public String createToken(Authentication authentication) {
		// 소셜 로그인 성공시 담긴 유저 정보를 꺼냅니다
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + tokenValidityInMilliseconds);

		return Jwts.builder()
			.setSubject(authentication.getName()) // 유저의 고유 식별자
			.claim("auth", authorities) // 권한 정보
			.signWith(key)
			.setExpiration(validity) // 토큰 만료 시간 설정
			.compact();
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
