//package vn.feature.service.impl;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//import vn.feature.response.product.ProductResponse;
//import vn.feature.service.ProductRedisService;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ProductRedisServiceImpl implements ProductRedisService {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private final ObjectMapper redisObjectMapper;
//
//    private String getKeyFrom(String keyword,
//                              Long categoryId,
//                              PageRequest pageRequest,
//                              String sortField,
//                              String sortDirection) {
//        int pageNumber = pageRequest.getPageNumber();
//        int pageSize = pageRequest.getPageSize();
//        return String.format(
//                "all_products:%s:%d:%d:%d:%s:%s",
//                keyword.toLowerCase(),
//                categoryId,
//                pageNumber,
//                pageSize,
//                sortField.toLowerCase(),
//                sortDirection.toLowerCase());
//    }
//
//
//    @Override
//    public void clear() {
//        redisTemplate.getConnectionFactory().getConnection().flushDb();
//    }
//
//    @Override
//    public List<ProductResponse> getAllProducts(String keyword,
//                                                Long categoryId,
//                                                PageRequest pageRequest,
//                                                String sortField,
//                                                String sortDirection) throws JsonProcessingException {
//        String key = this.getKeyFrom(keyword, categoryId, pageRequest, sortField, sortDirection);
//        String json = (String) redisTemplate.opsForValue().get(key);
//        return (json != null)
//                ? redisObjectMapper.readValue(json, new TypeReference<>() {})
//                : null;
//    }
//
//    @Override
//    public void saveAllProducts(List<ProductResponse> productResponses,
//                                String keyword,
//                                Long categoryId,
//                                PageRequest pageRequest,
//                                String sortField,
//                                String sortDirection) throws JsonProcessingException {
//
//        String key = this.getKeyFrom(keyword, categoryId, pageRequest, sortField, sortDirection);
//        String json = redisObjectMapper.writeValueAsString(productResponses);
//        redisTemplate.opsForValue().set(key, json);
//    }
//}
