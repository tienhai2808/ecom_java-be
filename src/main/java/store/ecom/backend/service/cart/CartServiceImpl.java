package store.ecom.backend.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Cart;
import store.ecom.backend.repository.CartItemRepository;
import store.ecom.backend.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  public Cart getCart(Long id) {
    return cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));
  }

  @Override
  public void clearCart(Long id) {
    Cart cart = getCart(id);
    cartItemRepository.deleteAllByCartId(id);
    cart.getItems().clear();
    cartRepository.deleteById(id);
  }

  @Override
  public BigDecimal getTotalPrice(Long id) {
    Cart cart = getCart(id);
    return cart.getTotalAmount();
  }

  @Override
  public Long initializeNewCart() {
    Cart newCart = new Cart();
    newCart.setTotalAmount(BigDecimal.ZERO);
    return cartRepository.save(newCart).getId();
  }
}
