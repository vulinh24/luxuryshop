package com.luxuryshop.repository;

import com.luxuryshop.data.tables.pojos.Collection;
import com.luxuryshop.data.tables.records.CollectionRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import static com.luxuryshop.data.Tables.COLLECTION;

@Repository
public class JCollectionRepository extends AbsRepository<CollectionRecord, Collection, Integer> {
    @Override
    protected TableImpl<CollectionRecord> getTable() {
        return COLLECTION;
    }
}
