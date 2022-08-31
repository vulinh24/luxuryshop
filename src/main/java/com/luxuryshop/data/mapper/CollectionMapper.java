package com.luxuryshop.data.mapper;

import com.luxuryshop.data.tables.pojos.Collection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CollectionMapper extends AbsMapper<Collection, Collection, Collection> {
}
