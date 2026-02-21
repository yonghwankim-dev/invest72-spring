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

public class PrincipalUser implements OidcUser {

	private final User user;
	private final Map<String, Object> attributes;

	private final OidcIdToken idToken;

	private final OidcUserInfo userInfo;

	public PrincipalUser(User user, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
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
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
	}

	/**
	 * 사용자의 고유 식별자를 반환하는 메서드입니다. 이 예제에서는 User 객체의 ID를 반환하도록 구현되어 있습니다.
	 * 객체의 ID는 UUID로 생성되어 고유하므로, 이를 사용하여 사용자를 식별할 수 있습니다.
	 * 이 메서드는 Spring Security에서 인증된 사용자의 이름을 가져올 때 사용됩니다.
	 * @return 사용자의 고유 식별자 (User 객체의 ID)
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
