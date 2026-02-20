package co.invest72.security;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import co.invest72.user.domain.User;
import co.invest72.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

	private final UserRepository userRepository;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		// 1. 부모 클래스의 loadUser를 호출하여 유저 정보를 가져옵니다.
		OidcUser oidcUser = super.loadUser(userRequest);

		try {
			return processOidcUser(oidcUser);
		} catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	private OidcUser processOidcUser(OidcUser oidcUser) {
		Map<String, Object> attributes = oidcUser.getAttributes();

		String email = (String)attributes.get("email");
		String name = (String)attributes.get("name");

		// 여기서 DB 저장 로직을 수행하세요 (예: 유저가 없으면 저장, 있으면 업데이트)
		String id = UUID.randomUUID().toString();
		User user = new User(id, email, name);
		userRepository.save(user);

		System.out.println("✅ OIDC 로그인 시도: " + email);

		return oidcUser;
	}
}
