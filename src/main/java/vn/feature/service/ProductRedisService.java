package vn.feature.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.PageRequest;
import vn.feature.response.product.ProductResponse;

import java.util.List;

public interface ProductRedisService {
    // clear cache data in redis
    void clear();

    List<ProductResponse> getAllProducts(String keyword,
                                         Long categoryId,
                                         PageRequest pageRequest,
                                         String sortField,
                                         String sortDirection) throws JsonProcessingException;

    void saveAllProducts(List<ProductResponse> productResponses,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest,
                         String sortField,
                         String sortDirection) throws JsonProcessingException;
}
