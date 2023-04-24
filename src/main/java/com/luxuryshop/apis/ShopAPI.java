package com.luxuryshop.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxuryshop.entities.*;
import com.luxuryshop.entities.primarykey.PKOfCart;
import com.luxuryshop.model.CartModel;
import com.luxuryshop.repositories.*;
import com.luxuryshop.services.MyUserDetail;
import com.luxuryshop.solve_exception.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ShopAPI {

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    VnpayDetailRepository vnpayDetailRepository;

    @SuppressWarnings("unchecked")
    @Transactional
    @RequestMapping(value = {"/choose-product-to-cart"}, method = RequestMethod.POST)
    public ResponseEntity<CartModel> muaHang(@RequestBody CartModel cartModel, Model model) throws Exception {
        HttpSession httpSession = request.getSession();
        String checkUser = (String) httpSession.getAttribute("USER");
        if (checkUser != null) {

            int numCart = (int) httpSession.getAttribute("NUM_CART");
            MyUserDetail userDetail = (MyUserDetail)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userDetail.getUser();
            Cart cart = new Cart();
            PKOfCart pk = new PKOfCart(user.getId(), cartModel.getProductId());
            Optional<Cart> optional = cartRepository.findById(pk);
            if (optional.isEmpty()) {
                cart.setProductCart(productRepository.findById(cartModel.getProductId()).get());
                cart.setUser(user);
                cart.setQuantity(cartModel.getQuantity());
                cart.setPk(pk);
                cartRepository.save(cart);
            } else {
                cart = optional.get();
                Integer preQuantity = cart.getQuantity();
                cart.setQuantity(preQuantity + cartModel.getQuantity());
                cartRepository.save(cart);
            }

            httpSession.setAttribute("NUM_CART", numCart + cartModel.getQuantity());
            model.addAttribute("cate", "shop");
            return new ResponseEntity<>(cartModel, new HttpHeaders(), HttpStatus.OK);
        } else {
            Map<Integer, Integer> CART = null;
            Integer numCart = 0;
            if (httpSession.getAttribute("gCART") == null) {
                CART = new HashMap<>();
            } else {
                CART = (Map<Integer, Integer>) httpSession.getAttribute("gCART");
                numCart = (Integer) httpSession.getAttribute("NUM_CART");
            }
            if (!CART.containsKey(cartModel.getProductId())) {
                CART.put(cartModel.getProductId(), cartModel.getQuantity());
            } else {
                int oldQuantity = CART.get(cartModel.getProductId());
                CART.put(cartModel.getProductId(), oldQuantity + cartModel.getQuantity());
            }
            numCart += cartModel.getQuantity();

            httpSession.setAttribute("NUM_CART", numCart);
            httpSession.setAttribute("gCART", CART);
            model.addAttribute("cate", "shop");
            return new ResponseEntity<>(cartModel, new HttpHeaders(), HttpStatus.OK);
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @RequestMapping(value = {"/change-product-cart"}, method = RequestMethod.POST)
    public ResponseEntity changeQuantity(@RequestBody String json, Model model) throws Exception {
        HttpSession session = request.getSession();
        ObjectMapper om = new ObjectMapper();
        CartModel cartModel = om.readValue(json, CartModel.class);
        if (session.getAttribute("USER") != null) {
            int numCart = (int) session.getAttribute("NUM_CART");
            MyUserDetail userDetail = (MyUserDetail)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userDetail.getUser();
            Product product = productRepository.findById(cartModel.getProductId()).get();
            if (product.getAmount() < cartModel.getQuantity()) return ResponseEntity.badRequest().build();
            PKOfCart pk = new PKOfCart(user.getId(), cartModel.getProductId());
            Cart cart = cartRepository.findById(pk).get();
            int preQuantity = cart.getQuantity();
            cart.setQuantity(cartModel.getQuantity());
            cartRepository.save(cart);
            om.writeValue(response.getOutputStream(), cartModel);

            session.setAttribute("NUM_CART", numCart + (cartModel.getQuantity() - preQuantity));
            model.addAttribute("cate", "shop");
        } else {
            Product product = productRepository.findById(cartModel.getProductId()).get();
            if (product.getAmount() < cartModel.getQuantity()) return ResponseEntity.badRequest().build();
            Map<Integer, Integer> CART = null;
            Integer numCart = 0;
            if (session.getAttribute("gCART") == null) {
                CART = new HashMap<>();
            } else {
                CART = (Map<Integer, Integer>) session.getAttribute("gCART");
                numCart = (Integer) session.getAttribute("NUM_CART");
            }
            int preQuantity = CART.get(cartModel.getProductId());
            CART.put(cartModel.getProductId(), cartModel.getQuantity());
            om.writeValue(response.getOutputStream(), cartModel);
            session.setAttribute("NUM_CART", numCart + (cartModel.getQuantity() - preQuantity));
            model.addAttribute("cate", "shop");
        }
        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @RequestMapping(value = "/remove-product/{id}", method = RequestMethod.GET)
    public String removeProduct(@PathVariable int id) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") != null) {
            MyUserDetail userDetail = (MyUserDetail)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userDetail.getUser();
            int numCart = (int) session.getAttribute("NUM_CART");
            PKOfCart pk = new PKOfCart(user.getId(), id);
            Cart cart = cartRepository.getById(pk);
            if (cart == null) throw new APIException("Không tìm thấy sản phẩm!");
            session.setAttribute("NUM_CART", numCart - cart.getQuantity());
            cartRepository.delete(cart);
            return "ok";
        } else {
            Map<Integer, Integer> CART = null;
            Integer numCart = 0;
            if (session.getAttribute("gCART") == null) {
                CART = new HashMap<>();
            } else {
                CART = (Map<Integer, Integer>) session.getAttribute("gCART");
                numCart = (Integer) session.getAttribute("NUM_CART");
            }
            if (!CART.containsKey(id)) throw new APIException("Không tìm thấy sản phẩm!");
            else {
                session.setAttribute("NUM_CART", numCart - CART.get(id));
                CART.remove(id);
            }
            return "ok";
        }
    }

    @RequestMapping(value = "/check-discount/{name}", method = RequestMethod.POST)
    public String checkdiscount(@PathVariable String name, HttpServletRequest request) {

        Discount check = discountRepository.findByName(name.toUpperCase());
        if (check != null) {
            return String.valueOf(check.getDiscount());
        } else return "notfound";
    }

    @PostMapping("/suggest-search")
    public String searchByKeyword(@RequestBody String keyword) {
//        keyword = "%" + keyword + "%";
        List<Product> products = productRepository.findByTitleContainingIgnoreCase(keyword);
        Map<Integer, ProductImages> map = products.stream()
                .collect(Collectors.toMap(product -> product.getId(), product -> product.getProductImages().get(0)));
        String resp = "<div id=\"search-list\">\n" +
                "    <div class=\"search-section\">\n";
        if (!products.isEmpty()) {
            for (Product product : products) {
                DecimalFormat df = new DecimalFormat("###,###,###");
                String price = df.format(product.getPrice());
                String format = "<div class=\"product-row\">\n" +
                        "<a href=\"%s\" class=\"detail\">" +
                        "            <img class=\"image-search-result\" src=\"%s\">\n" +
                        "            <div class=\"description\" ><span class=\"desc\">%s</span><br /><span class=\"price\">%s</span></div>\n" +
                        "        </div></a>";
                String item = String.format(format, "/shop-details/" + product.getSeo(), "/file/upload/" + map.get(product.getId()).getPath(), product.getTitle(), price);
                resp = resp.concat(item);
            }
        } else {
            String s = "<div class=\"product-row\">\n" +
                    "            <div class=\"description\" >Không tìm thấy sản phẩm phù hơp!</div>\n" +
                    "        </div>";
            resp = resp.concat(s);
        }
        resp = resp.concat("    </div>\n" +
                "</div>");
        return resp;
    }

    @GetMapping("/order/vnpay-detail/{id}")
    public VnpayDetail getVnPayDetail(@PathVariable Integer id, HttpServletRequest request) {
        VnpayDetail vnpayDetail = vnpayDetailRepository.findByOrderId(id);
        return vnpayDetail;
    }
}
