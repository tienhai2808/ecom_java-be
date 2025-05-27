package store.ecom.backend.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;

import store.ecom.backend.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>  {

}
