package store.ecom.backend.service.order;

import java.util.List;

import store.ecom.backend.dto.OrderDto;
import store.ecom.backend.model.Order;

public interface OrderService {
  Order placeOrder(Long userId);

  OrderDto getOrder(Long id);

  List<OrderDto> getUserOrders(Long userId);
}
