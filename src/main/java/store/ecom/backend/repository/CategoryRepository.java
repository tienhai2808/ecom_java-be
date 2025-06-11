package store.ecom.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.ecom.backend.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Category findByName(String name);

  boolean existsByName(String name);
}
