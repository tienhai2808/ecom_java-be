package store.ecom.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.ecom.backend.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  void deleteAllByCartId(Long id);
}
