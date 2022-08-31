package com.luxuryshop.data.mapper;

import com.luxuryshop.data.tables.pojos.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper extends AbsMapper<Category, Category, Category> {
}
