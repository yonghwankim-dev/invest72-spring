package co.invest72.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import co.invest72.user.domain.User;
import lombok.Getter;

public class PrincipalUser extends org.springframework.security.core.userdetails.User implements OidcUser {

	@Getter
	private final User user;
	private final Map<String, Object> attributes;

	private final OidcIdToken idToken;

	private final OidcUserInfo userInfo;

	public PrincipalUser(User user, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
		super(user.getId(), "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
		this.user = user;
		this.attributes = attributes;
		this.idToken = idToken;
		this.userInfo = userInfo;
	}

	// uuid를 가진 임시 user 객체를 생성하는 정적 팩토리 메서드입니다. 이 메서드는 OidcUser의 속성에서 필요한 정보를 추출하여 User 객체를 생성합니다.
	public static PrincipalUser create(String id) {
		User user = User.create(id);
		return new PrincipalUser(user, null, null, null);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return super.getAuthorities();
	}

	/**
	 * 사용자의 고유 식별자(Unique Identifier)를 반환합니다.
	 * 이 메서드는 Spring Security의 Principal 인터페이스 구현체로서,
	 * 인증된 사용자를 시스템 내에서 고유하게 식별할 수 있는 ID를 제공합니다.
	 * * @return 사용자의 고유 식별자 문자열
	 */
	@Override
	public String getName() {
		return user.getId();
	}

	@Override
	public Map<String, Object> getClaims() {
		return getAttributes();
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return userInfo;
	}

	@Override
	public OidcIdToken getIdToken() {
		return idToken;
	}

}
