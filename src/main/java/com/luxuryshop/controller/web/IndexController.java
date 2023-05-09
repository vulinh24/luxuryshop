package com.luxuryshop.controller.web;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.luxuryshop.entities.Product;
import com.luxuryshop.entities.User;
import com.luxuryshop.model.Statistic;
import com.luxuryshop.repositories.*;
import com.luxuryshop.services.MyUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.luxuryshop.solve_exception.CustomException;
import com.luxuryshop.entities.Cart;
import com.luxuryshop.entities.Collection;

@Controller
@RequestMapping(value = {"/", "/home"})
public class IndexController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    BannerRepository bannerRepository;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    ShopLogRepository shopLogRepository;

    @Autowired
    ProductsOrderRepository productOrderRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String index(Model model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        try {
            HttpSession session = request.getSession();
            session.removeAttribute(ShopController.FILTER_MODEL); //  = final FILTER_MODEL in shop controller
            String user = (String) session.getAttribute("USER");
            if (user != null) {
                session.removeAttribute("NUM_CART");
                List<Cart> carts = cartRepository.findByUserName(user);
                int num_cart = 0;
                for (Cart cart : carts) {
                    num_cart += cart.getQuantity();
                }
                session.setAttribute("NUM_CART", num_cart);
            }
            List<Integer> suggestProductIds;
            if (session.getAttribute("USER") != null) {
                MyUserDetail udetail = (MyUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User userModel = userRepository.findByUsername(udetail.getUser().getUsername());
                suggestProductIds = shopLogRepository.getSuggestProductIdForUserId(userModel.getId());
                if (suggestProductIds.size() < 10) {
                    List<Integer> bestSellingIds = productOrderRepository.statisticBestSellingProduct(10).stream()
                            .filter(item -> !suggestProductIds.contains(item.getKey()))
                            .map(Statistic::getKey)
                            .collect(Collectors.toList());
                    suggestProductIds.addAll(bestSellingIds);
                }
            } else {
                List<Statistic> statistics = productOrderRepository.statisticBestSellingProduct(10);
                suggestProductIds = statistics.stream().map(Statistic::getKey).collect(Collectors.toList());
            }
            List<Product> suggestProducts = suggestProductIds.stream().map(item -> productRepository.findById(item))
                    .map(item -> item.orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            model.addAttribute("productsSuggest", suggestProducts);
            model.addAttribute("blogs", blogRepository.findAll());
            model.addAttribute("productsHot", productRepository.findByIsHotTrue());
            model.addAttribute("productsNew", productRepository.findByIsNewTrue());
            model.addAttribute("productsSale", productRepository.findByIsSaleTrue());
            List<Collection> collections = collectionRepository.findAll();
            model.addAttribute("collections", collections);
            model.addAttribute("banners", bannerRepository.findAll());
            model.addAttribute("cate", "home");
            String preURL = (String) request.getSession().getAttribute("preURL");
            if (preURL != null) {
                request.getSession().removeAttribute("preURL");
                response.sendRedirect(request.getContextPath() + preURL);
                return null;
            } else return "front-end/index";
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException();
        }
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "front-end/error";
    }

}
