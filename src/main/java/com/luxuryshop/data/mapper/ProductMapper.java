package com.luxuryshop.data.mapper;

import com.luxuryshop.data.request.ProductRequest;
import com.luxuryshop.data.response.ProductResponse;
import com.luxuryshop.data.tables.pojos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper extends AbsMapper<ProductRequest, Product, Product> {

    @Mapping(target = "isHot", source = "product.ishot")
    @Mapping(target = "isNew", source = "product.isnew")
    @Mapping(target = "isSale", source = "product.issale")
    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "seo", source = "product.seo")
    public abstract ProductResponse toResponse(Product product, List<ProductImage> productImages,
                                               ProductDetail productDetail, Category category, Collection collection);
}
