package co.invest72.security;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(CorsConfigurationProperties.class)
public class OAuth2LoginSecurityConfig {

	private final OAuth2AuthenticationSuccessHandler successHandler;
	private final CustomOidcUserService customOidcUserService;
	private final CorsConfigurationProperties corsConfigurationProperties;
	@Value("${app.domain}")
	private String csrfCookieDomain;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 필터 등록
		http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class); // 인증 필터 이후에 실행

		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(configurer ->
				// 쿠키 기반의 CSRF 토큰 저장소 설정 (프론트엔드가 토큰을 읽을 수 있게 함)
				// withHttpOnlyFalse()로 설정하여 JavaScript에서 CSRF 토큰에 접근할 수 있도록 허용
				configurer.csrfTokenRepository(cookieCsrfTokenRepository(csrfCookieDomain))
					// 요청 헤더명 지정 (기본값은 X-XSRF-TOKEN)
					.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
			)
			.sessionManagement(session -> {
					session.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession);
					session.sessionCreationPolicy(IF_REQUIRED) // 세션 필요 시 생성
						.maximumSessions(1); // 중복 로그인 제한 옵션
				}
			)
			.authorizeHttpRequests(authorize ->
				// 1. 루트와 정적 리소스 파일들을 모두 허용합니다.
				authorize.requestMatchers("/", "/index.html", "/static/**", "/favicon.ico", "/error").permitAll()
					.requestMatchers("/investments/**").permitAll() // 투자 계산 페이지는 인증 없이 접근 허용
					.requestMatchers("/login/**", "/oauth2/**", "/error").permitAll()
					.anyRequest().authenticated())
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo
					.oidcUserService(customOidcUserService))
				.successHandler(successHandler))
			.logout(logout -> logout
				.logoutUrl("/api/v1/auth/logout")
				.logoutSuccessHandler((request, response, authentication) ->
					response.setStatus(HttpServletResponse.SC_OK)
				)
				.invalidateHttpSession(true) // 세션 무효화
			);
		return http.build();
	}

	// CORS 정책 설정
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(corsConfigurationProperties.getAllowedOrigins());
		configuration.setAllowedMethods(corsConfigurationProperties.getAllowedMethods());
		configuration.setAllowedHeaders(corsConfigurationProperties.getAllowedHeaders());
		configuration.setAllowCredentials(corsConfigurationProperties.getAllowCredentials());

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	private CookieCsrfTokenRepository cookieCsrfTokenRepository(String domain) {
		CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
		repository.setCookieCustomizer(cookie -> {
			cookie.domain(domain);
			cookie.path("/"); // 쿠키 경로 설정
			cookie.secure(true); // HTTPS에서만 전송
			cookie.httpOnly(false); // XSRF-TOKEN 쿠키값을 읽기 위함
			cookie.sameSite("Lax");
		});
		return repository;
	}
}
