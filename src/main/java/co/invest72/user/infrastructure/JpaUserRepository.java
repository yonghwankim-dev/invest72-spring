package co.invest72.user.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.invest72.user.domain.User;

public interface JpaUserRepository extends JpaRepository<User, String> {

	@Query("SELECT u FROM User u WHERE u.providerId = :providerId")
	Optional<User> findByProviderId(@Param("providerId") String providerId);
}
