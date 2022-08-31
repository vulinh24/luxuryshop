package com.luxuryshop.service;

import com.luxuryshop.data.mapper.CollectionMapper;
import com.luxuryshop.data.tables.pojos.Collection;
import com.luxuryshop.data.tables.records.CollectionRecord;
import com.luxuryshop.repository.JCollectionRepository;
import org.springframework.stereotype.Service;

@Service
public class CollectionService extends AbsService<CollectionRecord, Collection, Collection, Integer> {
    public CollectionService(JCollectionRepository baseRepo, CollectionMapper mapper) {
        super(baseRepo, mapper);
    }
}
