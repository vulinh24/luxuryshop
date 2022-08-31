package com.luxuryshop.repository;

import com.luxuryshop.data.tables.pojos.Category;
import com.luxuryshop.data.tables.records.CategoryRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import static com.luxuryshop.data.Tables.CATEGORY;

@Repository
public class JCategoryRepository extends AbsRepository<CategoryRecord, Category, Integer> {
    @Override
    protected TableImpl<CategoryRecord> getTable() {
        return CATEGORY;
    }
}
