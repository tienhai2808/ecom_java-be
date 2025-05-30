package store.ecom.backend.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import store.ecom.backend.dto.ImageDto;
import store.ecom.backend.model.Image;

public interface IImageService {
  Image getImageById(Long id);

  void deleteImageById(Long id);

  List<ImageDto> saveImages(List<MultipartFile> files, Long productId);

  void updateImage(MultipartFile file, Long id);
}
