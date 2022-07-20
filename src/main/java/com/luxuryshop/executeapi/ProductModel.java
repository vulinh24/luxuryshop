package com.luxuryshop.executeapi;

import java.util.ArrayList;
import java.util.List;

import com.luxuryshop.entities.ProductDetail;
import com.luxuryshop.entities.ProductImages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
	private String title;
	private float price;
	private float priceOld;
	private String shortDescription;
	private Boolean isHot;
	private Boolean isNew;
	private Boolean isSale;
	private Integer rate;
	private Integer categoryId;
	private List<ProductImages> productImages = new ArrayList<>();
	private Integer collectionId;
	private ProductDetail productDetail;
}
