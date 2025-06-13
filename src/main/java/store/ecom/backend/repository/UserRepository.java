package store.ecom.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.ecom.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  User findByEmail(String email);
}
