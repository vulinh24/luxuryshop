package com.luxuryshop.service;

import com.luxuryshop.data.mapper.BlogMapper;
import com.luxuryshop.data.tables.pojos.Blog;
import com.luxuryshop.data.tables.records.BlogRecord;
import com.luxuryshop.repository.JBlogRepository;
import org.springframework.stereotype.Service;

@Service
public class BlogService extends AbsService<BlogRecord, Blog, Blog, Integer> {
    public BlogService(JBlogRepository baseRepo, BlogMapper mapper) {
        super(baseRepo, mapper);
    }
}
