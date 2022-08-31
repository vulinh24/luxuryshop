package com.luxuryshop.service;

import com.luxuryshop.data.mapper.BannerMapper;
import com.luxuryshop.data.tables.pojos.Banner;
import com.luxuryshop.data.tables.records.BannerRecord;
import com.luxuryshop.repository.JBannerRepository;
import org.springframework.stereotype.Service;

@Service
public class BannerService extends AbsService<BannerRecord, Banner, Banner, Integer> {
    public BannerService(JBannerRepository baseRepo, BannerMapper mapper) {
        super(baseRepo, mapper);
    }
}
