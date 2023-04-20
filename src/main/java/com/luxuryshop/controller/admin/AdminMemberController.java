package com.luxuryshop.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.luxuryshop.entities.User;
import com.luxuryshop.services.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.luxuryshop.repositories.FProductRepository;
import com.luxuryshop.repositories.RoleUserRepository;
import com.luxuryshop.repositories.UserRepository;

import java.util.Random;

@Controller
public class AdminMemberController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleUserRepository roleUserRepository;

	@Autowired
	FProductRepository fProductRepository;

	@Autowired
	SendMailService sendMailService;

	@RequestMapping(value = { "/admin/customers" }, method = RequestMethod.GET)
	public String index(final ModelMap model, final HttpServletRequest request, final HttpServletResponse Response)
			throws Exception {
		model.addAttribute("users", userRepo.getCustomerUser());
		return "back-end/view_customers";
	}

	@Transactional
	@RequestMapping(value = { "/admin/customers-delete/{id}" }, method = RequestMethod.GET)
	public String index(@PathVariable("id") Integer id, final ModelMap model, final HttpServletRequest request,
						final HttpServletResponse Response) throws Exception {

		roleUserRepository.deleteUser(id);
		fProductRepository.deleteUser(id);
		userRepo.deleteById(id);
		return "redirect:/admin/customers";
	}

	@Transactional
	@RequestMapping(value = {"/admin/customer/reset-password/{id}"}, method = RequestMethod.GET)
	public String resetPassword(@PathVariable("id") Integer id, final HttpServletRequest request,
								final HttpServletResponse Response) throws Exception {
		User user = userRepo.findById(id).get();
		BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
		String password = getRandomPassword();
		user.setPassword(encode.encode(password));
		userRepo.save(user);
		sendMailService.sendMailResetPassword(password, user.getEmail());
		return "redirect:/admin/customers";
	}

	public static String getRandomPassword() {
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder password = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			int index = random.nextInt(allowedChars.length());
			password.append(allowedChars.charAt(index));
		}
		return password.toString();
	}
}