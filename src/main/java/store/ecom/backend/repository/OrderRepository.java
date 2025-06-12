package store.ecom.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.ecom.backend.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserId(Long userId);
}
