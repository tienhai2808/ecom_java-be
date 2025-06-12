package store.ecom.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {
  private Long id;
  private Long userId;
  private LocalDate date;
  private BigDecimal totalAmount;
  private String status;
  private List<OrderItemDto> items;
}
