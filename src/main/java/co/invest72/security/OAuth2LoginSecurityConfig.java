package co.invest72.security;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
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

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(IF_REQUIRED) // 세션 필요 시 생성
				.maximumSessions(1) // 중복 로그인 제한 옵션
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
				.deleteCookies("JSESSIONID") // 세션 쿠키 삭제
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

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();

		// 쿠키 설정
		cookieSerializer.setCookiePath("/");
		cookieSerializer.setCookieName("INVEST72-API_SESSION");
		cookieSerializer.setUseSecureCookie(true);
		cookieSerializer.setUseHttpOnlyCookie(true);
		cookieSerializer.setSameSite("None");

		return cookieSerializer;
	}
}
