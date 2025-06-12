package store.ecom.backend.service.product;

import java.util.List;

import store.ecom.backend.dto.ProductDto;
import store.ecom.backend.model.Product;
import store.ecom.backend.request.ProductAddRequest;
import store.ecom.backend.request.ProductUpdateRequest;

public interface ProductService {
  Product addProduct(ProductAddRequest request);

  Product getProductById(Long id);

  void deleteProductById(Long id);

  Product updateProduct(ProductUpdateRequest request, Long id);

  List<Product> getAllProducts();

  List<Product> getProductsByCategory(String category);

  List<Product> getProductsByBrand(String brand);

  List<Product> getProductsByCategoryAndBrand(String category, String brand);

  List<Product> getProductsByName(String name);

  List<Product> getProductsByBrandAndName(String brand, String name);

  Long countProductsByBrandAndName(String brand, String name);

  List<ProductDto> getConvertedProducts(List<Product> products);

  ProductDto convertToDto(Product product);
}
