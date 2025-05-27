package store.ecom.backend.controller;

import org.springframework.http.HttpHeaders;

import java.sql.SQLException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.dto.ImageDto;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Image;
import store.ecom.backend.response.ApiResponse;
import store.ecom.backend.service.image.ImageServiceImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
  private final ImageServiceImpl imageService;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
    try {
      List<ImageDto> imageDtos = imageService.saveImages(files, productId);
      return ResponseEntity.ok(new ApiResponse("Lưu hình ảnh sản phẩm thành công", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Lỗi khi lưu hình ảnh sản phẩm: " + e.getMessage(), null));
    }
  }

  @GetMapping("/image/download/{id}")
  public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws SQLException {
    Image image = imageService.getImageById(id);
    ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
        .body(resource);
  }

  @PutMapping("/image/{id}/update")
  public ResponseEntity<ApiResponse> updateImage(@PathVariable Long id, @RequestBody MultipartFile file) {
    try {
      Image image = imageService.getImageById(id);
      if (image != null) {
        imageService.updateImage(file, id);
        return ResponseEntity.ok(new ApiResponse("Cập nhật hình ảnh sản phẩm thành công", null));
      }
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse("Không tìm thấy hình ảnh sản phẩm: " + e.getMessage(), null));
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse("Lỗi khi cập nhật hình ảnh sản phẩm", null));
  }

  @DeleteMapping("/image/{id}/delete")
  public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id, @RequestBody MultipartFile file) {
    try {
      Image image = imageService.getImageById(id);
      if (image != null) {
        imageService.deleteImageById(id);
        return ResponseEntity.ok(new ApiResponse("Xóa hình ảnh sản phẩm thành công", null));
      }
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse("Không tìm thấy hình ảnh sản phẩm: " + e.getMessage(), null));
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse("Lỗi khi xóa hình ảnh sản phẩm", null));
  }
}
