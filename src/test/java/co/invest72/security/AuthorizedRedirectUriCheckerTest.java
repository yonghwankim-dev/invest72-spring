package co.invest72.security;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorizedRedirectUriCheckerTest {

	private AuthorizedRedirectUriChecker checker;

	@BeforeEach
	void setUp() {
		List<String> allowedOrigins = List.of("http://localhost:3000");
		checker = new AuthorizedRedirectUriChecker(allowedOrigins);
	}

	@DisplayName("URI 검사")
	@Test
	void check() {
		// given
		String uri = "http://localhost:3000";
		// when
		boolean actual = checker.check(uri);
		// then
		Assertions.assertThat(actual).isTrue();
	}

	@DisplayName("URI 검사 - 출처가 프로토콜이 다르면 false를 반환하여야 한다")
	@Test
	void check_whenProtocolIsDiff_thenReturnFalse() {
		// given
		String uri = "https://localhost:3000";
		// when
		boolean actual = checker.check(uri);
		// then
		Assertions.assertThat(actual).isFalse();
	}

	@DisplayName("URI 검사 - 허용되지 않는 도메인은 false를 반환하여야 한다")
	@Test
	void check_whenUriIsNotAllowedOrigin_thenReturnFalse() {
		// given
		String uri = "http://hacker.com";
		// when
		boolean actual = checker.check(uri);
		// then
		Assertions.assertThat(actual).isFalse();
	}
}
