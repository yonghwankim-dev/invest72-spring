package co.invest72.user.domain;

import java.time.LocalDateTime;

import co.invest72.financial_product.domain.IdGenerator;
import co.invest72.user.infrastructure.UserIdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
	@Id
	private String id;
	private String email;
	private String nickname;
	private String providerId; // sub
	private LocalDateTime createdAt;

	private static final IdGenerator idGenerator = new UserIdGenerator("user");

	@Builder
	public User(String email, String nickname, String providerId) {
		this.id = idGenerator.generateId();
		this.email = email;
		this.nickname = nickname;
		this.providerId = providerId;
		this.createdAt = LocalDateTime.now();
	}
}
