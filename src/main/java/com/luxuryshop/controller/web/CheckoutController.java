package com.luxuryshop.controller.web;

import com.luxuryshop.entities.*;
import com.luxuryshop.model.VnpayReturn;
import com.luxuryshop.repositories.*;
import com.luxuryshop.services.MyUserDetail;
import com.luxuryshop.services.VnpayPayment;
import com.luxuryshop.solve_exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    VnpayPayment vnpayPayment;

    @Autowired
    VnpayDetailRepository vnpayDetailRepository;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = {"/checkout"}, method = RequestMethod.GET)
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
                model.addAttribute("total", total);
                model.addAttribute("CART", carts);
                model.addAttribute("cate", "shop");
                model.addAttribute("detailOrder", new Order());
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
                model.addAttribute("total", total);
                model.addAttribute("CART", carts);
                model.addAttribute("cate", "shop");
                model.addAttribute("detailOrder", new Order());
                return "front-end/checkout";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException();
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @RequestMapping(value = {"/save-cart"}, method = RequestMethod.POST)
    public String save(@ModelAttribute(name = "detailOrder") Order detailOrder, final ModelMap model, final HttpServletRequest request, final HttpServletResponse response)
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
                    Discount mgg = discountRepository.findByName(ccode.toUpperCase());
                    discount = mgg.getDiscount();
                    mgg.setRemain(mgg.getRemain() - 1);
                    discountRepository.save(mgg);
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
                if (detailOrder.getPayment().equals("COD")) detailOrder.setPayment("COD");
                detailOrder = detailRepository.save(detailOrder);
                // save product in order
                List<OrderProduct> saledProducts = new ArrayList<>();
                for (Cart cart : carts) {
                    OrderProduct p = new OrderProduct();
                    p.setProductId(cart.getProductCart().getId());
                    p.setSelledDate(Date.valueOf(LocalDate.now()));
                    p.setProductTitle(cart.getProductCart().getTitle());
                    float price = cart.getProductCart().getPrice();
                    p.setProductPrice(price - price * discount);
                    p.setQuantity(cart.getQuantity());
                    p.setOrder(detailOrder);
                    saledProducts.add(p);
                    cart.getProductCart().setAmount(cart.getProductCart().getAmount() - cart.getQuantity());
                }
                proOrderRepository.saveAll(saledProducts);
                // xoá giỏ hàng đi
                cartRepository.deleteAll(carts);
                session.setAttribute("NUM_CART", 0);
                if (detailOrder.getPayment().equals("COD")) {
                    return "redirect:/shopping-cart?checkout=success";
                } else {
                    return "redirect:" + vnpayPayment.getUrlVnpayPage(request, detailOrder);
                }
            } else {
                detailOrder.setCode(null);
                detailOrder.setCreatedDate(LocalDateTime.now());
                detailOrder.setUser(null);
                float discount = 0;
                String ccode = request.getParameter("ccode");
                if (ccode != null && ccode != "") {
                    detailOrder.setCode(ccode);
                    Discount mgg = discountRepository.findByName(ccode.toUpperCase());
                    discount = mgg.getDiscount();
                    mgg.setRemain(mgg.getRemain() - 1);
                    discountRepository.save(mgg);
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
                if (detailOrder.getPayment().equals("COD")) detailOrder.setPayment("COD");
                detailOrder = detailRepository.save(detailOrder);
                // save product in order
                List<OrderProduct> saledProducts = new ArrayList<>();
                for (Cart cart : carts) {
                    OrderProduct p = new OrderProduct();
                    p.setProductId(cart.getProductCart().getId());
                    p.setSelledDate(Date.valueOf(LocalDate.now()));
                    p.setProductTitle(cart.getProductCart().getTitle());
                    float price = cart.getProductCart().getPrice();
                    p.setProductPrice(price - price * discount);
                    p.setQuantity(cart.getQuantity());
                    p.setOrder(detailOrder);
                    saledProducts.add(p);
                    cart.getProductCart().setAmount(cart.getProductCart().getAmount() - cart.getQuantity());
                }
                proOrderRepository.saveAll(saledProducts);
                // xoá giỏ hàng đi
                session.removeAttribute("gCART");
                session.setAttribute("NUM_CART", 0);
                if (detailOrder.getPayment().equals("COD")) {
                    return "redirect:/shopping-cart?checkout=success";
                } else {
                    return "redirect:" + vnpayPayment.getUrlVnpayPage(request, detailOrder);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException();
        }
    }

    @GetMapping("/vnpay-return")
    public String vnpayReturn(HttpServletRequest request, HttpServletResponse response, VnpayReturn vnpayReturn) throws RuntimeException {
        if (vnpayReturn.getVnp_TransactionStatus().equals("00")) {
            // success
            Order order = detailRepository.getById(Integer.valueOf(vnpayReturn.getVnp_TxnRef()));
            order.setPayStatus("Đã thanh toán");
            VnpayDetail vnpayDetail = toVnpayDetail(vnpayReturn);
            vnpayDetail.setOrder(order);
            vnpayDetailRepository.save(vnpayDetail);
            return "redirect:/shopping-cart?checkout=success";
        } else {
            //fail
            Order order = detailRepository.getById(Integer.valueOf(vnpayReturn.getVnp_TxnRef()));
            order.setPayStatus("Chưa thanh toán vnpay");
            VnpayDetail vnpayDetail = toVnpayDetail(vnpayReturn);
            vnpayDetail.setOrder(order);
            vnpayDetailRepository.save(vnpayDetail);
            return "redirect:/shopping-cart?checkout=fail";
        }
    }

    private VnpayDetail toVnpayDetail(VnpayReturn vnpayReturn) {
        VnpayDetail vnpayDetail = new VnpayDetail();
        vnpayDetail.setVnp_Amount(vnpayReturn.getVnp_Amount());
        vnpayDetail.setVnp_BankCode(vnpayReturn.getVnp_BankCode());
        vnpayDetail.setVnp_OrderInfo(vnpayReturn.getVnp_OrderInfo());
        vnpayDetail.setVnp_BankTranNo(vnpayReturn.getVnp_BankTranNo());
        vnpayDetail.setVnp_PayDate(vnpayReturn.getVnp_PayDate());
        vnpayDetail.setVnp_ResponseCode(vnpayReturn.getVnp_ResponseCode());
        vnpayDetail.setVnp_TransactionNo(vnpayReturn.getVnp_TransactionNo());
        vnpayDetail.setVnp_TransactionStatus(vnpayReturn.getVnp_TransactionStatus());
        return vnpayDetail;
    }
}
// todo
/*
- luồng chạy thanh toán đã xong , nhưng cần kiểm tra lại trạng thái đang lưu trong db , phải check lại và có thể có thêm 1 số nghiệp vụ cho khách hàng
- show vnpay detail trong cms


 */