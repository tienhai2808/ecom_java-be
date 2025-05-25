package store.ecom.backend.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.exceptions.AlreadyExistsException;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Category;
import store.ecom.backend.repository.category.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceImpl {
  private final CategoryRepository categoryRepository;

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm"));
  }

  @Override
  public Category getCategoryByName(String name) {
    return categoryRepository.findByName(name);
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public Category addCategory(Category category) {
    return Optional.ofNullable(category).filter(c -> !categoryRepository.existsByName(c.getName()))
        .map(categoryRepository::save).orElseThrow(() -> new AlreadyExistsException(category.getName() + "đã tồn tại"));
  }

  @Override
  public Category updateCategory(Category category, Long id) {
    return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
      oldCategory.setName(category.getName());
      return categoryRepository.save(oldCategory);
    }).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm"));
  }

  @Override
  public void deleteCategoryById(Long id) {
    categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,
        () -> new ResourceNotFoundException("Không tìm thấy danh mục sản phẩm"));
  }
}
