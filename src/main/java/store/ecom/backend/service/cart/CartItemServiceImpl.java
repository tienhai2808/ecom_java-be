package store.ecom.backend.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Cart;
import store.ecom.backend.model.CartItem;
import store.ecom.backend.model.Product;
import store.ecom.backend.repository.CartItemRepository;
import store.ecom.backend.repository.CartRepository;
import store.ecom.backend.service.product.ProductService;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
  private final CartItemRepository cartItemRepository;
  private final CartRepository cartRepository;
  private final ProductService productService;
  private final CartService cartService;

  @Override
  public void addItemToCart(Long cartId, Long productId, int quantity) {
    Cart cart = cartService.getCart(cartId);
    Product product = productService.getProductById(productId);
    CartItem cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
        .orElse(new CartItem());
    if (cartItem.getId() == null) {
      cartItem.setProduct(product);
      cartItem.setCart(cart);
      cartItem.setQuantity(quantity);
      cartItem.setUnitPrice(product.getPrice());
    } else {
      cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }
    cartItem.setTotalPrice();
    cart.addItem(cartItem);
    cartItemRepository.save(cartItem);
    cartRepository.save(cart);
  }

  @Override
  public void removeItemFromCart(Long cartId, Long productId) {
    Cart cart = cartService.getCart(cartId);
    CartItem itemToRemove = getCartItem(cartId, productId);
    cart.removeItem(itemToRemove);
    cartRepository.save(cart);
  }

  @Override
  public void updateItemQuantity(Long cartId, Long productId, int quantity) {
    Cart cart = cartService.getCart(cartId);
    cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
        .ifPresent(item -> {
          item.setQuantity(quantity);
          item.setUnitPrice(item.getProduct().getPrice());
          item.setTotalPrice();
        });
    BigDecimal totalAmount = cart.getItems().stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,
        BigDecimal::add);

    cart.setTotalAmount(totalAmount);
    cartRepository.save(cart);
  }

  @Override
  public CartItem getCartItem(Long cartId, Long productId) {
    Cart cart = cartService.getCart(cartId);
    return cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId))
        .findFirst().orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy item"));
  }
}
