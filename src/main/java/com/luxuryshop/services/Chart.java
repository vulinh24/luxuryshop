package com.luxuryshop.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxuryshop.repositories.ProductsOrderRepository;

@Service
public class Chart {
	
	@Autowired
	ProductsOrderRepository orderRepository;
	
	@SuppressWarnings("rawtypes")
	public Map count() {
		Map<LocalDate, Float> res = new HashMap<>();
		for(int i = 0; i <= 6; ++i) {
			LocalDate now = LocalDate.now();
			LocalDate date = now.minusDays(i);
			Float total = orderRepository.countByDate(Date.valueOf(date));
			res.put(date, total);
		}
		return res;
	}
}
