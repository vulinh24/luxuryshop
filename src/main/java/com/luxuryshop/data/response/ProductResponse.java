package com.luxuryshop.data.response;

import com.luxuryshop.data.tables.pojos.Category;
import com.luxuryshop.data.tables.pojos.Collection;
import com.luxuryshop.data.tables.pojos.ProductDetail;
import com.luxuryshop.data.tables.pojos.ProductImage;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ProductResponse {
    private Integer id;
    private String title;
    private float price;
    private float priceOld;
    private String shortDescription;
    private String detailDescription;
    private String seo;
    private Boolean isHot;
    private Boolean isNew;
    private Boolean isSale;
    private Integer rate;
    private Integer categoryId;
    private Category category;
    private Integer collectionId;
    private Collection collection;

    private List<ProductImage> productImages = new ArrayList<>();

    private ProductDetail productDetail;
    private Boolean liked = false;
//    private List<Cart> carts = new ArrayList<>(); // focus

    public boolean userLiked(String username) {
        return liked;
    }

}
