package vn.feature.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.feature.dtos.ProductDTO;
import vn.feature.dtos.ProductImageDTO;
import vn.feature.exception.payload.DataNotFoundException;
import vn.feature.exception.payload.InvalidParamException;
import vn.feature.mapper.ProductMapper;
import vn.feature.model.Category;
import vn.feature.model.Product;
import vn.feature.model.ProductImage;
import vn.feature.repositorys.CategoryRepository;
import vn.feature.repositorys.ProductImageRepository;
import vn.feature.repositorys.ProductRepository;
import vn.feature.response.product.ProductResponse;
import vn.feature.service.ProductService;
import vn.feature.util.MessageKeys;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductImageRepository productImageRepository;

    private final ProductMapper productMapper;


    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                    new DataNotFoundException(
                            MessageKeys.CATEGORY_NOT_FOUND  + " " + productDTO.getCategoryId()
                    )
                );

        Product savedProduct = productMapper.toProduct(productDTO);
        savedProduct.setCategory(existCategory);
        return productRepository.save(savedProduct);
    }

    @Override
    public Product getProductById(Long productId) throws DataNotFoundException {
        return productRepository
                .findById(productId)
                .orElseThrow(()->
                        new DataNotFoundException(MessageKeys.PRODUCT_NOT_FOUND + " " + productId)
                );
    }

    @Override
    public void clear() {

    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId,
                                                PageRequest pageRequest,
                                                String sortField,
                                                String sortDirection)  {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                direction, sortField
        );

        Page<Product> productPage = productRepository.searchProducts(keyword,categoryId,pageable);

        return productPage.map(ProductResponse::fromProduct);
    }

    @Override
    public void saveAllProducts(List<ProductResponse> productResponses, String keyword, Long categoryId, PageRequest pageRequest, String sortField, String sortDirection) throws JsonProcessingException {

    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existsProduct = getProductById(id);
        if(existsProduct != null){
            Category existsCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            (MessageKeys.CATEGORY_NOT_FOUND+ " " + productDTO.getCategoryId())
                    ));

            existsProduct.setName(productDTO.getName());
            existsProduct.setCategory(existsCategory);
            existsProduct.setPrice(productDTO.getPrice());
            existsProduct.setDescription(productDTO.getDescription());
            existsProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existsProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsProduct(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) {

        Product existsProduct =  productRepository
                .findById(productId)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.CATEGORY_NOT_FOUND + " " + productId));

        ProductImage productImage = ProductImage.builder()
                .product(existsProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // không cho insert quá 5 ảnh cho một sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images lest " +
                    ProductImage.MAXIMUM_IMAGES_PER_PRODUCT + " reached");
        }

        return productImageRepository.save(productImage);
    }

    @Override
    public Product getDetailProducts(long productId) throws DataNotFoundException {
        Optional<Product> optionalProduct = productRepository.getDetailProducts(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new DataNotFoundException((MessageKeys.PRODUCT_NOT_FOUND+ " " + productId));
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductByIds(productIds);
    }
}
