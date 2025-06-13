package store.ecom.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Cart;
import store.ecom.backend.model.User;
import store.ecom.backend.response.ApiResponse;
import store.ecom.backend.service.cart.CartItemService;
import store.ecom.backend.service.cart.CartService;
import store.ecom.backend.service.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart-items")
public class CartItemController {
  private final CartItemService cartItemService;
  private final CartService cartService;
  private final UserService userService;

  @PostMapping("/item/add")
  public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId, @RequestParam int quantity) {
    try {
      User user = userService.getAuthenticatedUser();
      Cart cart = cartService.initializeNewCart(user);
      cartItemService.addItemToCart(cart.getId(), productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Thêm item vào giỏ hàng thành công", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    } catch (JwtException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
  public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
    try {
      cartItemService.removeItemFromCart(cartId, productId);
      return ResponseEntity.ok(new ApiResponse("Xóa item khỏi giỏ hàng thành công", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/cart/{cartId}/item/{productId}/update")
  public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long productId,
      @RequestParam int quantity) {
    try {
      cartItemService.updateItemQuantity(cartId, productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Thêm số lượng item thành công", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
