package store.ecom.backend.service.cart;

import store.ecom.backend.model.CartItem;

public interface CartItemService {
  void addItemToCart(Long cartId, Long productId, int quantity);

  void removeItemFromCart(Long cartId, Long produtId);

  void updateItemQuantity(Long cartId, Long productId, int quantity);

  CartItem getCartItem(Long cartId, Long ProductId);
}
