package co.invest72.security;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2LoginSecurityConfig {

	private final OAuth2AuthenticationSuccessHandler successHandler;
	private final CustomOidcUserService customOidcUserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
			.authorizeHttpRequests(authorize ->
				// 1. 루트와 정적 리소스 파일들을 모두 허용합니다.
				authorize.requestMatchers("/", "/index.html", "/static/**", "/favicon.ico", "/error").permitAll()
					.requestMatchers("/login/**", "/oauth2/**", "/error").permitAll()
					.anyRequest().authenticated())
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo
					.oidcUserService(customOidcUserService))
				.successHandler(successHandler));
		return http.build();
	}

}
