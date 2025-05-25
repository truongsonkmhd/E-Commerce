package vn.feature.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.feature.model.ProductImage;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
}
