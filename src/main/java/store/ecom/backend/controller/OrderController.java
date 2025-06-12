package store.ecom.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.dto.OrderDto;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Order;
import store.ecom.backend.response.ApiResponse;
import store.ecom.backend.service.order.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
  private final OrderService orderService;

  @PostMapping("/order")
  public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
    try {
      Order order = orderService.placeOrder(userId);
      OrderDto convertedOrderDto = orderService.convertToDto(order);
      return ResponseEntity.ok(new ApiResponse("Tạo đơn hàng thành công", convertedOrderDto));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Không thể tạo đơn hàng: " + e.getMessage(), null));
    }
  }

  @GetMapping("/{id}/order")
  public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
    try {
      OrderDto order = orderService.getOrder(id);
      return ResponseEntity.ok(new ApiResponse("Lấy đơn hàng thành công", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/user/{userId}/orders")
  public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
    try {
      List<OrderDto> orders = orderService.getUserOrders(userId);
      return ResponseEntity.ok(new ApiResponse("Lấy đơn hàng của người dùng thành công", orders));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
