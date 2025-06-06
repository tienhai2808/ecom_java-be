package store.ecom.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.exceptions.AlreadyExistsException;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Category;
import store.ecom.backend.response.ApiResponse;
import store.ecom.backend.service.category.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllCategories() {
    try {
      List<Category> categories = categoryService.getAllCategories();
      return ResponseEntity.ok(new ApiResponse("Lấy dữ liệu thành công", categories));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Lỗi lấy danh sách danh mục sản phẩm" + e.getMessage(), null));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
    try {
      Category newCategory = categoryService.addCategory(name);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new ApiResponse("Thêm danh mục thành công", newCategory));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/category/{id}/category")
  public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
    try {
      Category category = categoryService.getCategoryById(id);
      return ResponseEntity.ok(new ApiResponse("Lấy danh mục thành công", category));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/category/{name}/category")
  public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
    try {
      Category category = categoryService.getCategoryByName(name);
      return ResponseEntity.ok(new ApiResponse("Lấy danh mục thành công", category));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/category/{id}/delete")
  public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
    try {
      categoryService.deleteCategoryById(id);
      return ResponseEntity.ok(new ApiResponse("Xoá danh mục thành công", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/category/{id}/update")
  public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
    try {
      Category updatedCategory = categoryService.updateCategory(category, id);
      return ResponseEntity.ok(new ApiResponse("Cập nhật danh mục thành công", updatedCategory));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
