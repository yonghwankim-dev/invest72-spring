package co.invest72.security;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorizedRedirectUriCheckerTest {

	private AuthorizedRedirectUriChecker checker;

	@BeforeEach
	void setUp() {
		List<String> allowedOrigins = new ArrayList<>();
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
		Assertions.assertThat(actual).isFalse();
	}
}
