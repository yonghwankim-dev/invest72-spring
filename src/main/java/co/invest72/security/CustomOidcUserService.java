package co.invest72.security;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.user.domain.User;
import co.invest72.user.domain.UserRepository;
import co.invest72.user.domain.UuidGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOidcUserService extends OidcUserService {

	private final UserRepository userRepository;
	private final UuidGenerator uuidGenerator;

	@Override
	@Transactional
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		// 1. 부모 클래스의 loadUser를 호출하여 유저 정보를 가져옵니다.
		OidcUser oidcUser = super.loadUser(userRequest);
		String providerId = oidcUser.getSubject(); // 구글의 sub값

		User user = userRepository.findByProviderId(providerId).orElseGet(() -> saveNewUser(oidcUser, providerId));

		return new PrincipalUser(user, oidcUser.getAttributes(), oidcUser.getIdToken(), oidcUser.getUserInfo());
	}

	private User saveNewUser(OidcUser oidcUser, String providerId) {
		// 2. 유저가 존재하지 않으면 새로 생성하여 저장합니다.
		String uuid = uuidGenerator.generate(); // UUID 생성
		String email = oidcUser.getEmail(); // 구글에서 제공하는 이메일 정보
		String nickname = oidcUser.getGivenName();
		User newUser = new User(uuid, email, nickname, providerId);
		userRepository.save(newUser);
		return newUser;
	}
}
