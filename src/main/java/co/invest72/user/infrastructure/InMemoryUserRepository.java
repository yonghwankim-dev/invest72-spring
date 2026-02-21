package co.invest72.user.infrastructure;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import co.invest72.user.domain.User;
import co.invest72.user.domain.UserRepository;

@Repository
public class InMemoryUserRepository implements UserRepository {

	private final Map<String, User> userStore = new ConcurrentHashMap<>();

	@Override
	public void save(User user) {
		userStore.put(user.getId(), user);
	}

	@Override
	public Optional<User> findByProviderId(String providerId) {
		return userStore.values().stream()
			.filter(user -> user.getProviderId().equals(providerId))
			.findFirst();
	}

	@Override
	public Optional<User> findById(String id) {
		return Optional.ofNullable(userStore.get(id));
	}
}
