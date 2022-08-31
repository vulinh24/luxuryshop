package com.luxuryshop.data.mapper;

import com.luxuryshop.data.tables.pojos.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends AbsMapper<User, User, User> {
}
