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

public interface ProductRedisService {

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
