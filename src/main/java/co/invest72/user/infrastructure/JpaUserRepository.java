package co.invest72.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import co.invest72.user.domain.User;

public interface JpaUserRepository extends JpaRepository<User, String> {

}
