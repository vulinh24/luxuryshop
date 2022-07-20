package com.luxuryshop.controller.web;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.luxuryshop.SolveException.CustomException;
import com.luxuryshop.entities.Cart;
import com.luxuryshop.entities.DetailOrder;
import com.luxuryshop.entities.Product;
import com.luxuryshop.entities.SaledProduct;
import com.luxuryshop.entities.User;
import com.luxuryshop.repositories.CartRepository;
import com.luxuryshop.repositories.DetailOrderRepository;
import com.luxuryshop.repositories.DiscountRepository;
import com.luxuryshop.repositories.ProductRepository;
import com.luxuryshop.repositories.ProductsOrderRepository;
import com.luxuryshop.repositories.UserRepository;
import com.luxuryshop.services.MyUserDetail;

@Controller
public class CheckoutController {

	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	DetailOrderRepository detailRepository;
	
	@Autowired
	ProductsOrderRepository proOrderRepository;
	
	@Autowired
	DiscountRepository discountRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/checkout" }, method = RequestMethod.GET)
	public String index(Model model, final HttpServletRequest request) throws Exception {
		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("USER") != null) {
				MyUserDetail userDetail = null;
				User user = null;
				List<Cart> carts = null;
				try {
					userDetail = (MyUserDetail) 
							SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					user = userDetail.getUser();
					carts = cartRepository.findByUserName(user.getUsername());
				} catch (Exception e) {
					e.printStackTrace();
					throw new CustomException();
				}
				
				float total = 0;
				for (Cart cart : carts) {
					total += cart.getQuantity() * cart.getProductCart().getPrice();
				}
				
				model.addAttribute("user", user);
				model.addAttribute("total",total);
				model.addAttribute("CART",carts);
				model.addAttribute("cate", "shop");
				model.addAttribute("detailOrder", new DetailOrder());
				return "front-end/checkout";
			} else {
				List<Cart> carts = new ArrayList<>();
				Map<Integer, Integer> gCART = (Map<Integer, Integer>) session.getAttribute("gCART");
				if (gCART != null) {
					Set<Integer> proIds = gCART.keySet();
					for (Integer prodId : proIds) {
						Product prod = productRepository.getById(prodId);
						Cart cart = new Cart();
						cart.setProductCart(prod);
						cart.setQuantity(gCART.get(prodId));
						carts.add(cart);
					}
				}
				
				float total = 0;
				for (Cart cart : carts) {
					total += cart.getQuantity() * cart.getProductCart().getPrice();
				}
				
				model.addAttribute("user", new User());
				model.addAttribute("total",total);
				model.addAttribute("CART",carts);
				model.addAttribute("cate", "shop");
				model.addAttribute("detailOrder", new DetailOrder());
				return "front-end/checkout";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@RequestMapping(value = { "/save-cart" }, method = RequestMethod.POST)
	public String save(@ModelAttribute(name = "detailOrder") DetailOrder detailOrder,final ModelMap model, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("USER") != null) {
				MyUserDetail udetail = (MyUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				User user = userRepository.findByUsername(udetail.getUser().getUsername());
				detailOrder.setCode(null);
				detailOrder.setCreatedDate(LocalDateTime.now());
				detailOrder.setUser(user);
				float discount = 0;
				String ccode = request.getParameter("ccode");
				if (ccode != null && ccode != "") {
					detailOrder.setCode(ccode);
					discount = discountRepository.findByName(ccode.toUpperCase()).getDiscount();
				}
				
				List<Cart> carts = cartRepository.findByUserName(udetail.getUser().getUsername());
				float tong = 0;
				for (Cart cart : carts) {
					tong += cart.getQuantity() * cart.getProductCart().getPrice();
				}
				tong = tong - tong * discount;
				detailOrder.setTotal(tong);
				detailOrder.setTotalReceived(tong);
				detailOrder.setStatus("Chờ xác nhận");
				detailOrder.setCreatedDate(LocalDateTime.now());
				detailOrder = detailRepository.save(detailOrder);
				// save product in order
				List<SaledProduct> saledProducts = new ArrayList<>();
				for (Cart cart : carts) {
					SaledProduct p = new SaledProduct();
					p.setSelledDate(Date.valueOf(LocalDate.now()));
					p.setProductTitle(cart.getProductCart().getTitle());
					float price = cart.getProductCart().getPrice();
					p.setProductPrice(price - price * discount);
					p.setQuantity(cart.getQuantity());
					p.setOrder(detailOrder);
					saledProducts.add(p);
				}
				proOrderRepository.saveAll(saledProducts);
				// xoá giỏ hàng đi
				cartRepository.deleteAll(carts);
				session.setAttribute("NUM_CART", 0);
				return "redirect:/shopping-cart?checkout=success";
			} else {
				detailOrder.setCode(null);
				detailOrder.setCreatedDate(LocalDateTime.now());
				detailOrder.setUser(null);
				float discount = 0;
				String ccode = request.getParameter("ccode");
				if (ccode != null && ccode != "") {
					detailOrder.setCode(ccode);
					discount = discountRepository.findByName(ccode.toUpperCase()).getDiscount();
				}
				
				List<Cart> carts = new ArrayList<>();
				Map<Integer, Integer> gCART = (Map<Integer, Integer>) session.getAttribute("gCART");
				if (gCART != null) {
					Set<Integer> proIds = gCART.keySet();
					for (Integer prodId : proIds) {
						Product prod = productRepository.getById(prodId);
						Cart cart = new Cart();
						cart.setProductCart(prod);
						cart.setQuantity(gCART.get(prodId));
						carts.add(cart);
					}
				}
				
				float tong = 0;
				for (Cart cart : carts) {
					tong += cart.getQuantity() * cart.getProductCart().getPrice();
				}
				tong = tong - tong * discount;
				detailOrder.setTotal(tong);
				detailOrder.setTotalReceived(tong);
				detailOrder.setStatus("Chờ xác nhận");
				detailOrder.setCreatedDate(LocalDateTime.now());
				detailOrder = detailRepository.save(detailOrder);
				// save product in order
				List<SaledProduct> saledProducts = new ArrayList<>();
				for (Cart cart : carts) {
					SaledProduct p = new SaledProduct();
					p.setSelledDate(Date.valueOf(LocalDate.now()));
					p.setProductTitle(cart.getProductCart().getTitle());
					float price = cart.getProductCart().getPrice();
					p.setProductPrice(price - price * discount);
					p.setQuantity(cart.getQuantity());
					p.setOrder(detailOrder);
					saledProducts.add(p);
				}
				proOrderRepository.saveAll(saledProducts);
				// xoá giỏ hàng đi
				session.removeAttribute("gCART");
				session.setAttribute("NUM_CART", 0);
				return "redirect:/shopping-cart?checkout=success";
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new CustomException();
		}
	}
}
