package store.ecom.backend.service.product;

import java.util.List;

import store.ecom.backend.model.Product;
import store.ecom.backend.request.product.ProductAddRequest;
import store.ecom.backend.request.product.ProductUpdateRequest;

public interface IProductService {
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
}
