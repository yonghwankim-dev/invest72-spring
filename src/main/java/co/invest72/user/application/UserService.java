package co.invest72.user.application;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.invest72.user.domain.User;
import co.invest72.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;

	/**
	 * 사용자 ID로 사용자 정보 조회
	 * @param id 사용자 ID
	 * @return 사용자 정보
	 * @throws IllegalArgumentException 사용자 ID가 존재하지 않을 경우
	 */
	@Transactional(readOnly = true)
	@Cacheable(value = "userMe", key = "#id")
	public User getUserById(String id) {
		return repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));
	}
}
