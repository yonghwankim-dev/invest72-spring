package co.invest72.user.domain;

import java.util.Optional;

public interface UserRepository {
	User save(User user);

	Optional<User> findByProviderId(String providerId);

	Optional<User> findById(String id);
}
