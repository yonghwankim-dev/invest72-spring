package co.invest72.user.domain;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class User {
	private String id;
	private String email;
	private String nickname;
	private LocalDateTime createdAt;

	public User(String id, String email, String nickname) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.createdAt = LocalDateTime.now();
	}
}
