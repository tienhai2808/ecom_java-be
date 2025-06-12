package store.ecom.backend.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.dto.CategoryDto;
import store.ecom.backend.dto.ImageDto;
import store.ecom.backend.dto.ProductDto;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Category;
import store.ecom.backend.model.Image;
import store.ecom.backend.model.Product;
import store.ecom.backend.repository.CategoryRepository;
import store.ecom.backend.repository.ImageRepository;
import store.ecom.backend.repository.ProductRepository;
import store.ecom.backend.request.product.ProductAddRequest;
import store.ecom.backend.request.product.ProductUpdateRequest;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  @Override
  public Product addProduct(ProductAddRequest request) {
    Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
        .orElseGet(() -> {
          Category newCategory = new Category(request.getCategory().getName());
          return categoryRepository.save(newCategory);
        });

    request.setCategory(category);

    return productRepository.save(createProduct(request, category));
  }

  private Product createProduct(ProductAddRequest request, Category category) {
    return new Product(
        request.getName(),
        request.getBrand(),
        request.getPrice(),
        request.getInventory(),
        request.getDescription(),
        category);
  }

  @Override
  public Product getProductById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
  }

  @Override
  public void deleteProductById(Long id) {
    productRepository.findById(id).ifPresentOrElse(productRepository::delete,
        () -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
  }

  @Override
  public Product updateProduct(ProductUpdateRequest request, Long id) {
    return productRepository.findById(id).map(existingProduct -> updateExistingProduct(existingProduct, request))
        .map(productRepository::save).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
  }

  private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
    existingProduct.setName(request.getName());
    existingProduct.setBrand(request.getBrand());
    existingProduct.setPrice(request.getPrice());
    existingProduct.setInventory(request.getInventory());
    existingProduct.setDescription(request.getDescription());

    Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
        .orElseGet(() -> {
          Category newCategory = new Category(request.getCategory().getName());
          return categoryRepository.save(newCategory);
        });

    existingProduct.setCategory(category);

    return productRepository.save(existingProduct);
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public List<Product> getProductsByCategory(String category) {
    return productRepository.findByCategoryName(category);
  }

  @Override
  public List<Product> getProductsByBrand(String brand) {
    return productRepository.findByBrand(brand);
  }

  @Override
  public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
    return productRepository.findByCategoryNameAndBrand(category, brand);
  }

  @Override
  public List<Product> getProductsByName(String name) {
    return productRepository.findByName(name);
  }

  @Override
  public List<Product> getProductsByBrandAndName(String brand, String name) {
    return productRepository.findByBrandAndName(brand, name);
  }

  @Override
  public Long countProductsByBrandAndName(String brand, String name) {
    return productRepository.countByBrandAndName(brand, name);
  }

  @Override
  public List<ProductDto> getConvertedProducts(List<Product> products) {
    return products.stream().map(this::convertToDto).toList();
  }

  @Override
  public ProductDto convertToDto(Product product) {
    ProductDto productDto = modelMapper.map(product, ProductDto.class);
    CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
    productDto.setCategory(categoryDto);
    return productDto;
  }
}
