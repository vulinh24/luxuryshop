//package com.luxuryshop.service;
//
//import com.luxuryshop.data.mapper.ProductMapper;
//import com.luxuryshop.data.request.ProductRequest;
//import com.luxuryshop.data.response.ProductResponse;
//import com.luxuryshop.data.tables.pojos.*;
//import com.luxuryshop.data.tables.records.ProductRecord;
//import com.luxuryshop.model.ProductFilterModel;
//import com.luxuryshop.repository.JCategoryRepository;
//import com.luxuryshop.repository.JCollectionRepository;
//import com.luxuryshop.repository.JProductRepository;
//import org.jooq.Condition;
//import org.jooq.OrderField;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.luxuryshop.data.Tables.PRODUCT;
//import static java.util.stream.Collectors.toList;
//
//@Service
//public class ProductService extends AbsService<ProductRecord, ProductRequest, Product, Integer> {
//
//    private final JProductRepository baseRepo;
//    private final ProductMapper mapper;
//    private final JCategoryRepository categoryRepository;
//    private final JCollectionRepository collectionRepository;
//
//    public ProductService(JProductRepository baseRepo,
//                          ProductMapper mapper,
//                          JCategoryRepository categoryRepository,
//                          JCollectionRepository collectionRepository) {
//        super(baseRepo, mapper);
//        this.baseRepo = baseRepo;
//        this.mapper = mapper;
//        this.categoryRepository = categoryRepository;
//        this.collectionRepository = collectionRepository;
//    }
//
//    public List<Product> getFavoriteProduct(Integer userId) {
//        return baseRepo.getFavoriteProduct(userId);
//    }
//
//    public ProductResponse getBySeo(String seo) {
//        Product product = baseRepo.getList(PRODUCT.SEO.eq(seo)).get(0);
//        List<ProductImage> productImages = baseRepo.getProductImages(product.getId());
//        ProductDetail detail = baseRepo.getDetail(product.getId());
//        Category category = categoryRepository.findById(product.getCategoryId()).get();
//        Collection collection = collectionRepository.findById(product.getCollectionId()).get();
//        return mapper.toResponse(product, productImages, detail, category, collection);
//    }
//
//    public List<ProductResponse> getByConditionResponse(String username, Condition... conditions) {
//        List<Product> products = baseRepo.getList(conditions);
//        return toListResponse(products, username);
//    }
//
//    public ProductDetail getDetail(Integer productId) {
//        return baseRepo.getDetail(productId);
//    }
//
//    public List<ProductImage> getProductImages(Integer productId) {
//        return baseRepo.getProductImages(productId);
//    }
//
//    public List<ProductResponse> search(ProductFilterModel productFilter, String username) {
//        List<Condition> conditions = new ArrayList<>();
//        OrderField orderField = null;
//        if (productFilter.getCategoryId() != null)
//            conditions.add(PRODUCT.CATEGORY_ID.eq(productFilter.getCategoryId()));
//        if (productFilter.getCollectionId() != null)
//            conditions.add(PRODUCT.COLLECTION_ID.eq(productFilter.getCollectionId()));
//        if (productFilter.getBeginPrice() != null)
//            conditions.add(PRODUCT.PRICE.ge(productFilter.getBeginPrice()));
//        if (productFilter.getEndPrice() != null)
//            conditions.add(PRODUCT.PRICE.le(productFilter.getEndPrice()));
//        if (productFilter.getSearchKeyword() != null)
//            conditions.add(PRODUCT.TITLE.likeIgnoreCase(productFilter.getSearchKeyword()));
//        if (productFilter.getTag() != null)
//            conditions.add(PRODUCT.TITLE.likeIgnoreCase(productFilter.getTag()));
//        if (productFilter.getSort() != null && productFilter.getSort().equals("asc"))
//            orderField = PRODUCT.PRICE.asc();
//        if (productFilter.getSort() != null && productFilter.getSort().equals("desc"))
//            orderField = PRODUCT.PRICE.desc();
//        if (productFilter.getCurrentPage() == null) productFilter.setCurrentPage(1);
//        int offset = (productFilter.getCurrentPage() - 1) * NUM_OF_PRODUCTS;
//        List<Product> products = baseRepo.getList(orderField, offset, NUM_OF_PRODUCTS, conditions);
//        if (productFilter.getCurrentPage() == 1) {
//            productFilter.setSize(products.size());
//            productFilter.setTotalPage((int) Math.ceil((double) productFilter.getSize() / NUM_OF_PRODUCTS));
//        }
//        return toListResponse(products, username);
//    }
//
//    private List<ProductResponse> toListResponse(List<Product> products, String username) {
//        return products.stream()
//                .map(product -> {
//                    List<ProductImage> productImages = baseRepo.getProductImages(product.getId());
//                    ProductDetail detail = baseRepo.getDetail(product.getId());
//                    Category category = categoryRepository.findById(product.getCategoryId()).get();
//                    Collection collection = collectionRepository.findById(product.getCollectionId()).get();
//                    ProductResponse productResponse = mapper.toResponse(product, productImages, detail, category, collection);
//                    Long liked = baseRepo.getLiked(product.getId(), username);
//                    if (liked >= 1) productResponse.setLiked(true);
//                    return productResponse;
//                })
//                .collect(toList());
//    }
//}
