package com.luxuryshop.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luxuryshop.entities.User;

@Service
@RequestMapping(value = { "/login" })
public class AdminLoginController {

	@GetMapping
	public String getLogin(final ModelMap model, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		String error = request.getParameter("page_error");
		if (error == null) {
			String preUrl = request.getHeader("Referer");
			request.getSession().setAttribute("preURL", preUrl);
			model.addAttribute("user", new User());
			return "back-end/login";
		} else {
			model.addAttribute("user", new User());
			return "back-end/login";
		}
	}
}
