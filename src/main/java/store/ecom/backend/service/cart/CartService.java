package store.ecom.backend.service.cart;

import java.math.BigDecimal;

import store.ecom.backend.dto.CartDto;
import store.ecom.backend.model.Cart;
import store.ecom.backend.model.User;

public interface CartService {
  Cart getCart(Long id);

  void clearCart(Long id);

  BigDecimal getTotalPrice(Long id);

  Cart initializeNewCart(User user);

  Cart getCartByUserId(Long userId);

  CartDto convertToDto(Cart cart);
}
