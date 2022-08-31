package com.luxuryshop.data.mapper;

import com.luxuryshop.data.tables.pojos.Banner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class BannerMapper extends AbsMapper<Banner, Banner, Banner> {
}
