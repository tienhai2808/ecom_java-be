package store.ecom.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.ecom.backend.model.Cart;


public interface CartRepository extends JpaRepository<Cart, Long> {
  Cart findByUserId(Long userId);
}
