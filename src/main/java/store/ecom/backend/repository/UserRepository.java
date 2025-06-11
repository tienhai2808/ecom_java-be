package store.ecom.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.ecom.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);
}
