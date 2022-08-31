package com.luxuryshop.data.mapper;

import com.luxuryshop.data.tables.pojos.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ContactMapper extends AbsMapper<Contact, Contact, Contact> {
}
