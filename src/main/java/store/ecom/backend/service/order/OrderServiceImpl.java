package store.ecom.backend.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.dto.OrderDto;
import store.ecom.backend.enums.OrderStatus;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Cart;
import store.ecom.backend.model.Order;
import store.ecom.backend.model.OrderItem;
import store.ecom.backend.model.Product;
import store.ecom.backend.repository.OrderRepository;
import store.ecom.backend.repository.ProductRepository;
import store.ecom.backend.service.cart.CartService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final CartService cartService;
  private final ModelMapper modelMapper;

  @Transactional
  @Override
  public Order placeOrder(Long userId) {
    Cart cart = cartService.getCartByUserId(userId);
    Order newOrder = createOrder(cart);

    List<OrderItem> orderItems = createOrderItems(newOrder, cart);
    newOrder.setItems(new HashSet<>(orderItems));
    newOrder.setTotalAmount(calculateTotalAmount(orderItems));
    Order savedOrder = orderRepository.save(newOrder);

    cartService.clearCart(cart.getId());

    return savedOrder;
  }

  private Order createOrder(Cart cart) {
    Order order = new Order();
    order.setStatus(OrderStatus.PENDING);
    order.setDate(LocalDate.now());
    order.setUser(cart.getUser());
    return order;
  }

  private List<OrderItem> createOrderItems(Order order, Cart cart) {
    return cart.getItems().stream().map(item -> {
      Product product = item.getProduct();
      product.setInventory(product.getInventory() - item.getQuantity());
      productRepository.save(product);

      return new OrderItem(
          item.getQuantity(),
          item.getUnitPrice(),
          order,
          product);
    }).toList();
  }

  private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
    return orderItemList.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  public OrderDto getOrder(Long id) {
    return orderRepository.findById(id).map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
  }

  @Override
  public List<OrderDto> getUserOrders(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    return orders.stream().map(this::convertToDto).toList();
  }

  @Override
  public OrderDto convertToDto(Order order) {
    return modelMapper.map(order, OrderDto.class);
  }
}
