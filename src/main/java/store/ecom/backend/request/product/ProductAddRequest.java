package store.ecom.backend.request.product;

import java.math.BigDecimal;

import lombok.Data;
import store.ecom.backend.model.Category;

@Data
public class ProductAddRequest {
  private Long id;
  private String name;
  private String brand;
  private BigDecimal price;
  private int inventory;
  private String description;
  private Category category;
}
