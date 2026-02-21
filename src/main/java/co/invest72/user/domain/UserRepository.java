package co.invest72.user.domain;

import java.util.Optional;

public interface UserRepository {
	void save(User user);

	Optional<User> findByProviderId(String providerId);
}
