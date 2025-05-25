package vn.feature.mapper;

import org.mapstruct.Mapper;
import vn.feature.dtos.ProductDTO;
import vn.feature.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductDTO productDTO);
}
