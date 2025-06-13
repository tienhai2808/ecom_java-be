package store.ecom.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.ecom.backend.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String role);
}
