package com.luxuryshop.repository;

import com.luxuryshop.data.tables.pojos.Contact;
import com.luxuryshop.data.tables.records.ContactRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import static com.luxuryshop.data.Tables.CONTACT;

@Repository
public class JContactRepository extends AbsRepository<ContactRecord, Contact, Integer> {
    @Override
    protected TableImpl<ContactRecord> getTable() {
        return CONTACT;
    }
}
