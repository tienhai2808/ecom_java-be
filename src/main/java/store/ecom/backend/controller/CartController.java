package store.ecom.backend.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Cart;
import store.ecom.backend.response.ApiResponse;
import store.ecom.backend.service.cart.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
  private final CartService cartService;

  @GetMapping("/{id}/my-cart")
  public ResponseEntity<ApiResponse> getCart(@PathVariable Long id) {
    try {
      Cart cart = cartService.getCart(id);
      return ResponseEntity.ok(new ApiResponse("Lấy giỏ hàng thành công", cart));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{id}/clear")
  public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id) {
    try {
      cartService.clearCart(id);
      return ResponseEntity.ok(new ApiResponse("Xóa giỏ hàng thành công", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{id}/cart/total-price")
  public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long id) {
    try {
      BigDecimal totalPrice = cartService.getTotalPrice(id);
      return ResponseEntity.ok(new ApiResponse("Lấy tổng giỏ hàng thành công", totalPrice));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
  
}
