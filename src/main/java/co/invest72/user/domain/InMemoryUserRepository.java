package co.invest72.user.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {

	private final Map<String, User> userStore = new ConcurrentHashMap<>();

	@Override
	public void save(User user) {
		userStore.put(user.getId(), user);
	}
}
