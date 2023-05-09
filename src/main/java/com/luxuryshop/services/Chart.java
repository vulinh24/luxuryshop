package com.luxuryshop.services;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javassist.scopedpool.SoftValueHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luxuryshop.repositories.ProductsOrderRepository;

@Service
public class Chart {
	
	@Autowired
	ProductsOrderRepository orderRepository;
	
	@SuppressWarnings("rawtypes")
	public Map count() {
		Map<LocalDate, Float> res = new LinkedHashMap<>();
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.minus(2, ChronoUnit.WEEKS);
		for (LocalDate date = today; !date.isBefore(startDate); date = date.minusDays(1)) {
			Float total = orderRepository.countByDate(Date.valueOf(date));
			res.put(date, total);
		}
		return res;
	}
}
