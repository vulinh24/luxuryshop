package com.luxuryshop.repository;

import com.luxuryshop.data.tables.pojos.Banner;
import com.luxuryshop.data.tables.records.BannerRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Repository;

import static com.luxuryshop.data.Tables.BANNER;

@Repository
public class JBannerRepository extends AbsRepository<BannerRecord, Banner, Integer> {
    @Override
    protected TableImpl<BannerRecord> getTable() {
        return BANNER;
    }
}
