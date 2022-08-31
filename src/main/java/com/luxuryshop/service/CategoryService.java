package com.luxuryshop.service;

import com.luxuryshop.data.mapper.CategoryMapper;
import com.luxuryshop.data.tables.pojos.Category;
import com.luxuryshop.data.tables.records.CategoryRecord;
import com.luxuryshop.repository.JCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends AbsService<CategoryRecord, Category, Category, Integer> {
    public CategoryService(JCategoryRepository baseRepo, CategoryMapper mapper) {
        super(baseRepo, mapper);
    }
}
