package store.ecom.backend.repository.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.ecom.backend.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>  {
  List<Image> findByProductId(Long id);
}
