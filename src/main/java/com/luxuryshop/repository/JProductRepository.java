package com.luxuryshop.repository;

import com.luxuryshop.data.tables.pojos.Product;
import com.luxuryshop.data.tables.pojos.ProductDetail;
import com.luxuryshop.data.tables.pojos.ProductImage;
import com.luxuryshop.data.tables.records.ProductRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.luxuryshop.data.Tables.*;

@Repository
public class JProductRepository extends AbsRepository<ProductRecord, Product, Integer> {
    @Override
    protected TableImpl<ProductRecord> getTable() {
        return PRODUCT;
    }

    public List<Product> getFavoriteProduct(Integer userId) {
        return dslContext.select(getTable().fields())
                .from(getTable()).join(FAVORITE_PRODUCT).on(FAVORITE_PRODUCT.PRODUCT_ID.eq(PRODUCT.ID))
                .where(FAVORITE_PRODUCT.USER_ID.eq(userId))
                .fetchInto(Product.class);
    }

    public List<ProductImage> getProductImages(Integer productId) {
        return dslContext.selectFrom(PRODUCT_IMAGE)
                .where(PRODUCT_IMAGE.PRODUCT_ID.eq(productId))
                .fetchInto(ProductImage.class);
    }

    public ProductDetail getDetail(Integer productId) {
        return dslContext.selectFrom(PRODUCT_DETAIL)
                .where(PRODUCT_DETAIL.PRODUCT_ID.eq(productId))
                .fetchOneInto(ProductDetail.class);
    }

    public Long getLiked(Integer productId, String username) {
        return dslContext.select(DSL.count())
                .from(FAVORITE_PRODUCT).join(USER).on(FAVORITE_PRODUCT.USER_ID.eq(USER.ID))
                .where(FAVORITE_PRODUCT.PRODUCT_ID.eq(productId).and(USER.USERNAME.eq(username)))
                .fetchOneInto(Long.class);
    }
}
