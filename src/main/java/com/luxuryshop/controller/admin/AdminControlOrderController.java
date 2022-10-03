package com.luxuryshop.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxuryshop.entities.Order;
import com.luxuryshop.entities.OrderProduct;
import com.luxuryshop.entities.Product;
import com.luxuryshop.repositories.DetailOrderRepository;
import com.luxuryshop.repositories.ProductRepository;
import com.luxuryshop.repositories.UserRepository;
import com.luxuryshop.solve_exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AdminControlOrderController {
	@Autowired
	DetailOrderRepository orderRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@RequestMapping(value = { "/admin/orders" }, method = RequestMethod.GET)
	public String index(final ModelMap model, final HttpServletRequest request, final HttpServletResponse Response)
			throws Exception {
		try {
			Sort sort = Sort.by(Direction.DESC, "createdDate");
			List<Order> orders = orderRepository.findAll(sort);
			model.addAttribute("orders", orders);
			return "back-end/view_orders";
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException();
		}
	}

	@ResponseBody
	@RequestMapping(value = {"/admin/is-cancel"}, method = RequestMethod.POST)
	public void cancel(final ModelMap model, final HttpServletRequest request, final HttpServletResponse response,
					   @RequestBody Order data) throws Exception {
		ObjectMapper om = new ObjectMapper();
		Order order = orderRepository.getById(data.getId());
		List<OrderProduct> soldProduct = order.getSaledOrder();
		soldProduct.forEach(orderProduct -> {
			Integer productId = orderProduct.getProductId();
			Product prod = productRepository.getById(productId);
			prod.setAmount(prod.getAmount() + orderProduct.getQuantity());
		});
		order.setUpdatedDate(LocalDateTime.now());
		order.setIsCancel(true);
		order.setStatus("Đã hủy");
		orderRepository.save(order);

		om.writeValue(response.getOutputStream(), "ok");

	}

	@ResponseBody
	@RequestMapping(value = {"/admin/is-pay"}, method = RequestMethod.POST)
	@Transactional
	public void confirm(final ModelMap model, final HttpServletRequest request, final HttpServletResponse response,
						@RequestBody Order data) throws Exception {
		ObjectMapper om = new ObjectMapper();
		Order order = orderRepository.getById(data.getId());
		order.setUpdatedDate(LocalDateTime.now());
		order.setStatus("Đang giao hàng");
		orderRepository.save(order);
		om.writeValue(response.getOutputStream(), "ok");
	}
}
