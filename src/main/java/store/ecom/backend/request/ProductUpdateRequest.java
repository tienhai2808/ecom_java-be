package store.ecom.backend.request;

import java.math.BigDecimal;

import lombok.Data;
import store.ecom.backend.model.Category;

@Data
public class ProductUpdateRequest {
  private String name;
  private String brand;
  private BigDecimal price;
  private int inventory;
  private String description;
  private Category category;
}
