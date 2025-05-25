package vn.feature.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.feature.dtos.ProductDTO;
import vn.feature.model.Product;
import vn.feature.response.ApiResponse;
import vn.feature.response.product.ProductPageResponse;
import vn.feature.response.product.ProductResponse;
import vn.feature.service.ProductService;
import vn.feature.service.impl.ProductServiceImpl;
import vn.feature.util.MessageKeys;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

//    private final ProductRedisService productRedisService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult bindingResult
            ){
        try{
            if(bindingResult.hasErrors()){
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errorMessages.stream()
                        .toList()
                );
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(
                    ApiResponse.builder().success(true)
                            .message((MessageKeys.CREATE_PRODUCT_SUCCESS))
                            .payload(newProduct)
                            .build()
            );
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error((MessageKeys.CREATE_PRODUCT_FAILED)).build());
        }
    }

//    @GetMapping("")
//    public ResponseEntity<ProductPageResponse> getProduct(
//            @RequestParam(defaultValue = "", name = "keyword") String keyword,
//            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
//            @RequestParam(defaultValue = "0", name = "page") int page,
//            @RequestParam(defaultValue = "10", name = "limit") int limit,
//            @RequestParam(defaultValue = "id", name = "sort_field") String sortField,
//            @RequestParam(defaultValue = "asc", name = "sort_direction") String sortDirection
//    ) throws JsonProcessingException {
//        PageRequest pageRequest = PageRequest.of(page, limit);
//
//        List<ProductResponse> productResponses = productRedisService.getAllProducts(
//                keyword, categoryId, pageRequest, sortField, sortDirection);
//
//        if (productResponses == null) {
//            Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest, sortField, sortDirection);
//            List<ProductResponse> products = productPage.getContent();
//
//            // Lưu sản phẩm vào Redis cache nếu không tìm thấy trong Redis
//            productRedisService.saveAllProducts(products, keyword, categoryId, pageRequest, sortField, sortDirection);
//
//            return ResponseEntity.ok(ProductPageResponse.builder()
//                    .products(products)
//                    .pageNumber(page)
//                    .totalElements(productPage.getTotalElements())
//                    .pageSize(productPage.getSize())
//                    .isLast(productPage.isLast())
//                    .totalPages(productPage.getTotalPages())
//                    .build());
//        }
//        // Trường hợp tìm thấy sản phẩm trong Redis
//        Page<ProductResponse> productPage = new PageImpl<>(
//                productResponses, pageRequest, productResponses.size());
//        return ResponseEntity.ok(ProductPageResponse.builder()
//                .products(productResponses)
//                .pageNumber(page)
//                .totalElements(productPage.getTotalElements())
//                .pageSize(productPage.getSize())
//                .isLast(productPage.isLast())
//                .totalPages(productPage.getTotalPages())
//                .build());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        try {
            Product existsProducts = productService.getProductById(id);
            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .payload(ProductResponse.fromProduct(existsProducts)).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message((MessageKeys.GET_INFORMATION_FAILED))
                    .error(e.getMessage()).build()
            );
        }
    }
}
