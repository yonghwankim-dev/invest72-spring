package co.invest72.user.domain;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

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
}
