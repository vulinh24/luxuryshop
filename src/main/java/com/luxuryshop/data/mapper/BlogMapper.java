package com.luxuryshop.data.mapper;

import com.luxuryshop.data.tables.pojos.Blog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class BlogMapper extends AbsMapper<Blog, Blog, Blog> {
}
