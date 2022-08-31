package com.luxuryshop.repository;

import com.luxuryshop.data.tables.pojos.Blog;
import com.luxuryshop.data.tables.records.BlogRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import static com.luxuryshop.data.Tables.BLOG;

@Repository
public class JBlogRepository extends AbsRepository<BlogRecord, Blog, Integer> {
    @Override
    protected TableImpl<BlogRecord> getTable() {
        return BLOG;
    }
}
