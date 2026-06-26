package co.invest72.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.user.domain.User;
import co.invest72.user.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomOidcUserService extends OidcUserService {

	private final UserRepository userRepository;
	private final String adminEmail;

	public CustomOidcUserService(UserRepository userRepository, @Value("${admin.user.email:none}") String adminEmail) {
		this.userRepository = userRepository;
		this.adminEmail = adminEmail;
	}

	@Transactional
	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		// 1. 부모 클래스의 loadUser를 호출하여 유저 정보를 가져옵니다.
		OidcUser oidcUser = super.loadUser(userRequest);
		String providerId = oidcUser.getSubject(); // 구글의 sub값

		User user = userRepository.findByProviderId(providerId).orElseGet(() -> saveNewUser(oidcUser, providerId));
		Set<GrantedAuthority> roles = new HashSet<>(
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
		if (isAdminEmail(user.getEmail())) {
			roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		return new PrincipalUser(user, oidcUser.getAttributes(), oidcUser.getIdToken(), oidcUser.getUserInfo(), roles);
	}

	private boolean isAdminEmail(String email) {
		return adminEmail.equals(email);
	}

	private User saveNewUser(OidcUser oidcUser, String providerId) {
		// 2. 유저가 존재하지 않으면 새로 생성하여 저장합니다.
		String email = oidcUser.getEmail(); // 구글에서 제공하는 이메일 정보
		String nickname = oidcUser.getGivenName();
		User newUser = new User(email, nickname, providerId);
		return userRepository.save(newUser);
	}
}
