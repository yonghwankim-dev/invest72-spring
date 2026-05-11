package co.invest72.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorizedRedirectUriCheckerTest {

	@DisplayName("객체 생성")
	@Test
	void canCreated() {
		// when
		AuthorizedRedirectUriChecker checker = new AuthorizedRedirectUriChecker();
		// then
		Assertions.assertThat(checker).isNotNull();
	}
}
