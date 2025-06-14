package store.ecom.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.dto.ProductDto;
import store.ecom.backend.exceptions.AlreadyExistsException;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Product;
import store.ecom.backend.request.ProductAddRequest;
import store.ecom.backend.request.ProductUpdateRequest;
import store.ecom.backend.response.ApiResponse;
import store.ecom.backend.service.product.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
  private final ProductService productService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllProducts() {
    try {
      List<Product> products = productService.getAllProducts();
      List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("Lấy dữ liệu thành công", convertedProducts));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Lỗi lấy danh sách sản phẩm: " + e.getMessage(), null));
    }
  }

  @GetMapping("/product/{id}/product")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
    try {
      Product product = productService.getProductById(id);
      ProductDto convertedProductDto = productService.convertToDto(product);
      return ResponseEntity.ok(new ApiResponse("Lấy sản phẩm thành công", convertedProductDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductAddRequest product) {
    try {
      Product newProduct = productService.addProduct(product);
      ProductDto convertedProductDto = productService.convertToDto(newProduct);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new ApiResponse("Thêm sản phẩm thành công", convertedProductDto));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/product/{id}/update")
  public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product, @PathVariable Long id) {
    try {
      Product updatedProduct = productService.updateProduct(product, id);
      return ResponseEntity.ok(new ApiResponse("Cập nhật sản phẩm thành công", updatedProduct));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/product/{id}/delete")
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
    try {
      productService.deleteProductById(id);
      return ResponseEntity.ok(new ApiResponse("Xóa sản phẩm thành công", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by-brand-and-name")
  public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
    try {
      List<Product> products = productService.getProductsByBrandAndName(brand, name);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse("Không tìm thấy sản phẩm với thương hiệu và tên đã cho", null));
      }
      List<ProductDto> conveProductDtos = productService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("Lấy sản phẩm thành công", conveProductDtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Lỗi lấy sản phẩm theo tên và thương hiệu: " + e.getMessage(), null));
    }
  }

  @GetMapping("/by-category-and-brand")
  public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category,
      @RequestParam String brand) {
    try {
      List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse("Không tìm thấy sản phẩm với danh mục sản phẩm và thương hiệu đã cho", null));
      }
      List<ProductDto> conveProductDtos = productService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("Lấy sản phẩm thành công", conveProductDtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Lỗi lấy sản phẩm theo danh mục sản phẩm và thương hiệu: " + e.getMessage(), null));
    }
  }

  @GetMapping("/{name}/products")
  public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
    try {
      List<Product> products = productService.getProductsByName(name);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse("Không tìm thấy sản phẩm với tên đã cho", null));
      }
      List<ProductDto> conveProductDtos = productService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("Lấy sản phẩm thành công", conveProductDtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Lỗi lấy sản phẩm theo tên: " + e.getMessage(), null));
    }
  }

  @GetMapping("/by-brand")
  public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand) {
    try {
      List<Product> products = productService.getProductsByBrand(brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse("Không tìm thấy sản phẩm với thương hiệu đã cho", null));
      }
      List<ProductDto> convertedProductDtos = productService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("Lấy sản phẩm thành công", convertedProductDtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Lỗi lấy sản phẩm theo thương hiệu: " + e.getMessage(), null));
    }
  }

  @GetMapping("/product/{category}/all/products")
  public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category) {
    try {
      List<Product> products = productService.getProductsByCategory(category);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse("Không tìm thấy sản phẩm với danh mục đã cho", null));
      }
      List<ProductDto> converteProductDtos = productService.getConvertedProducts(products);
      return ResponseEntity.ok(new ApiResponse("Lấy sản phẩm thành công", converteProductDtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Lỗi lấy sản phẩm theo danh mục: " + e.getMessage(), null));
    }
  }

  @GetMapping("/product/count/by-brand-and-name")
  public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,
      @RequestParam String name) {
    try {
      var productCount = productService.countProductsByBrandAndName(brand, name);
      return ResponseEntity.ok(new ApiResponse("Đếm sản phẩm thành công", productCount));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Lỗi đếm sản phẩm theo thương hiệu và tên: " + e.getMessage(), null));
    }
  }
}
