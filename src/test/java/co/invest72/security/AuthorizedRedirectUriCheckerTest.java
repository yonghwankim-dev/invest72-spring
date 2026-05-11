package co.invest72.security;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AuthorizedRedirectUriCheckerTest {

	private AuthorizedRedirectUriChecker checker;

	@BeforeEach
	void setUp() {
		List<String> allowedOrigins = List.of("http://localhost:3000");
		checker = new AuthorizedRedirectUriChecker(allowedOrigins);
	}

	@DisplayName("URI 검사 - 프로토콜, 호스트, 포트가 대소문자 관계없이 일치하면 true를 반환해야 한다")
	@Test
	void check_whenOriginIsMatched_thenReturnTrue() {
		// given
		String uri = "http://localhost:3000";
		// when
		boolean actual = checker.check(uri);
		// then
		Assertions.assertThat(actual).isTrue();
	}

	@DisplayName("URI 검사 - 프로토콜, 호스트, 포트가 다르면 false를 반환해야 한다")
	@ParameterizedTest
	@ValueSource(strings = {"https://localhost:3000", "http://hacker.com:3000", "http://localhost:4000"})
	void check_whenOriginIsDiff_thenReturnFalse(String uri) {
		// when
		boolean actual = checker.check(uri);
		// then
		Assertions.assertThat(actual).isFalse();
	}

	@DisplayName("URI 검사 - uri가 null이면 false를 반환해야 한다")
	@Test
	void check_whenUriIsNull_thenReturnFalse() {
		// given
		String uri = null;
		// when
		boolean actual = checker.check(uri);
		// then
		Assertions.assertThat(actual).isFalse();
	}
}
