package co.invest72.user.infrastructure;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import co.invest72.user.domain.User;
import co.invest72.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Primary
public class UserRepositoryImpl implements UserRepository {

	private final JpaUserRepository jpaUserRepository;

	@Override
	public void save(User user) {
		jpaUserRepository.save(user);
	}

	@Override
	public Optional<User> findByProviderId(String providerId) {
		return jpaUserRepository.findByProviderId(providerId);
	}

	@Override
	public Optional<User> findById(String id) {
		return jpaUserRepository.findById(id);
	}
}
