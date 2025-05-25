package vn.feature.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vn.feature.dtos.ProductDTO;
import vn.feature.dtos.ProductImageDTO;
import vn.feature.model.Product;
import vn.feature.model.ProductImage;
import vn.feature.response.product.ProductResponse;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductById(Long productId) throws Exception;

    // clear cache data in redis
    void clear();

    Page<ProductResponse> getAllProducts(String keyword,
                                         Long categoryId,
                                         PageRequest pageRequest,
                                         String sortField,
                                         String sortDirection)  ;

    void saveAllProducts(List<ProductResponse> productResponses,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest,
                         String sortField,
                         String sortDirection) throws JsonProcessingException;

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id);

    boolean existsProduct(String name);

    ProductImage createProductImage(Long productId,
                                    ProductImageDTO productImageDTO);

    Product getDetailProducts(long productId) throws Exception;

    List<Product> findProductsByIds(List<Long> productIds);
}
