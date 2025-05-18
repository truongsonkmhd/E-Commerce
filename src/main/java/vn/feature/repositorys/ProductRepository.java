package vn.feature.repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.feature.model.Product;
import vn.feature.util.ConfigSql;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable);

    @Query(ConfigSql.Product.SEARCH_PRODUCT_BY_KEYWORD)
    Page<Product> searchProducts(@Param("keyword") String keyword,
                                 @Param("categoryId") Long categoryId,
                                 Pageable pageable);

    @Query(ConfigSql.Product.GET_DETAIL_PRODUCT)
    Optional<Product> getDetailProducts(@Param("productId") Long productId);

    @Query(ConfigSql.Product.FIND_PRODUCT_BY_IDS)
    List<Product> findProductByIds(@Param("productIds") List<Long> productIds);

}
