package com.luxuryshop.controller.admin;

import com.luxuryshop.entities.Product;
import com.luxuryshop.model.Statistic;
import com.luxuryshop.model.StatisticRevenue;
import com.luxuryshop.repositories.DetailOrderRepository;
import com.luxuryshop.repositories.ProductRepository;
import com.luxuryshop.repositories.ProductsOrderRepository;
import com.luxuryshop.repositories.ShopLogRepository;
import com.luxuryshop.services.Chart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class AdminStatisticController {
    @Autowired
    Chart chart;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShopLogRepository shopLogRepository;

    @Autowired
    ProductsOrderRepository productOrderRepository;

    @Autowired
    DetailOrderRepository detailOrderRepository;

    @RequestMapping(value = {"/super-admin/statistic"}, method = RequestMethod.GET)
    public String index(final ModelMap model, final HttpServletRequest request, final HttpServletResponse Response)
            throws Exception {
        //get 20 best selling product
        List<Statistic> statistics = productOrderRepository.statisticBestSellingProduct(20);
        List<Product> bestSellingProducts = statistics.stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getKey()).orElse(null);
                    if (product == null) return null;
                    Product p = new Product();
                    p.setId(product.getId());
                    p.setTitle(product.getTitle());
                    p.setPrice(product.getPrice());
                    p.setPriceOld(product.getPriceOld());
                    p.setAmount(item.getValue());
                    return p;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        model.addAttribute("sell_products", bestSellingProducts);
        // get most viewed product
        List<Statistic> mostViewed = shopLogRepository.getMostViewedProduct();
        List<Product> mostViewedProduct = mostViewed.stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getKey()).orElse(null);
                    if (product == null) return null;
                    Product p = new Product();
                    p.setId(product.getId());
                    p.setTitle(product.getTitle());
                    p.setPrice(product.getPrice());
                    p.setPriceOld(product.getPriceOld());
                    p.setAmount(item.getValue());
                    return p;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        model.addAttribute("view_products", mostViewedProduct);
        return "back-end/statistic";
    }

    @ResponseBody
    @RequestMapping(value = "/caculate", method = RequestMethod.GET)
    public ResponseEntity cacul() {
        return ResponseEntity.ok(chart.count());
    }

    @ResponseBody
    @RequestMapping(value = "/statistic-revenue", method = RequestMethod.GET)
    public List<StatisticRevenue> statisticRevenues(HttpServletRequest request) {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<StatisticRevenue> result = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            Integer numberSold = productOrderRepository.countSoldProductAtDate(Date.valueOf(date));
            if (numberSold == null) numberSold = 0;
            Float revenue = detailOrderRepository.revenueAtDate(Date.valueOf(date));
            if (revenue == null) revenue = 0.0f;
            StatisticRevenue statisticRevenue = new StatisticRevenue();
            statisticRevenue.setDate(date);
            statisticRevenue.setQuantitySold(numberSold);
            statisticRevenue.setRevenue(revenue);
            result.add(statisticRevenue);
        }
        return result;
    }
}
