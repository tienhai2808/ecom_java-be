package store.ecom.backend.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.dto.ImageDto;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Image;
import store.ecom.backend.model.Product;
import store.ecom.backend.repository.image.ImageRepository;
import store.ecom.backend.service.product.ProductService;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final ImageRepository imageRepository;
  private final ProductService productService;

  @Value("${api.prefix}")
  private String apiPrefix;

  @Override
  public Image getImageById(Long id) {
    return imageRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hình ảnh sản phẩm"));
  }

  @Override
  public void deleteImageById(Long id) {
    imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
        () -> new ResourceNotFoundException("Không tìm thấy hình ảnh sản phẩm"));
  }

  @Override
  public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
    Product product = productService.getProductById(productId);
    List<ImageDto> savedImageDtos = new ArrayList<>();
    for (MultipartFile file : files) {
      try {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setImage(new SerialBlob(file.getBytes()));
        image.setProduct(product);

        String prefixDownloadUrl = apiPrefix + "/images/image/download/";
        String downloadUrl = prefixDownloadUrl + image.getId();
        image.setDownloadUrl(downloadUrl);

        Image savedImage = imageRepository.save(image);
        savedImage.setDownloadUrl(prefixDownloadUrl + savedImage.getId());
        imageRepository.save(savedImage);

        ImageDto imageDto = new ImageDto();
        imageDto.setImageId(savedImage.getId());
        imageDto.setFileName(savedImage.getFileName());
        imageDto.setDownloadUrl(savedImage.getDownloadUrl());

        savedImageDtos.add(imageDto);
      } catch (IOException | SQLException e) {
        new RuntimeException("Lỗi khi lưu hình ảnh sản phẩm: " + e.getMessage());
      }
    }
    return savedImageDtos;
  }

  @Override
  public void updateImage(MultipartFile file, Long id) {
    Image image = getImageById(id);
    try {
      image.setFileName(file.getOriginalFilename());
      image.setFileType(file.getContentType());
      image.setImage(new SerialBlob(file.getBytes()));
      imageRepository.save(image);
    } catch (IOException | SQLException e) {
      throw new RuntimeException("Lỗi khi cập nhật hình ảnh sản phẩm: " + e.getMessage());
    }
  }
}
