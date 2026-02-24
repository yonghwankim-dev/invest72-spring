package co.invest72.user.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
	@Id
	private String id;
	private String email;
	private String nickname;
	private String providerId; // sub
	private LocalDateTime createdAt;

	public User(String id, String email, String nickname, String providerId) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.providerId = providerId;
		this.createdAt = LocalDateTime.now();
	}

	/**
	 * id 값만이 초기화된 User 객체를 생성하여 반환한다
	 * 나머지 필드는 빈 문자열로 초기화된다.
	 * 이 메서드는 OidcUser의 속성에서 필요한 정보를 추출하여 User 객체를 생성하는 데 사용될 수 있다.
	 * @param id 사용자의 고유 식별자(예: UUID 값)를 나타내는 문자열입니다. 이 값은 사용자를 시스템 내에서 고유하게 식별하는 데 사용됩니다.
	 * @return id 값이 초기화된 User 객체를 반환합니다. 나머지 필드는 빈 문자열로 초기화됩니다.
	 */
	public static User create(String id) {
		return new User(id, "", "", "");
	}
}
