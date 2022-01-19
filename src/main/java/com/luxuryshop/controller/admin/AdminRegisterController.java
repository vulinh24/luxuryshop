package com.luxuryshop.controller.admin;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.luxuryshop.SolveException.CustomException;
import com.luxuryshop.entities.RoleOfUser;
import com.luxuryshop.entities.User;
import com.luxuryshop.entities.primarykey.PKOfRoleUser;
import com.luxuryshop.repositories.RoleUserRepository;
import com.luxuryshop.repositories.UserRepository;

@Controller
public class AdminRegisterController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	RoleUserRepository roleUserRepository;

	@RequestMapping(value = { "/registion" }, method = RequestMethod.POST)
	public String registion(final ModelMap model, final HttpServletRequest request, @Valid @ModelAttribute User userModel, BindingResult br)
			throws Exception {
		try {
			if (br.hasErrors()) {
				model.addAttribute("user",userModel);
				return "back-end/login";
			} else {
				String password_again = request.getParameter("password-again");
				User user = userRepo.findByUsername(userModel.getUsername());
				if (user == null) {
					if (userModel.getPassword().equals(password_again)) {
						userModel.setCreatedDate(LocalDateTime.now());
						userModel.setEnabled(true);
						userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
					} else {
						model.addAttribute("notsimilar", true);
						return "back-end/login";
					}
					userModel = userRepo.save(userModel);
					PKOfRoleUser pk = new PKOfRoleUser(2,userModel.getId());
					roleUserRepository.save(new RoleOfUser(pk));
					model.addAttribute("successr", true);
					return "back-end/login";
				} else {
					model.addAttribute("userexist", true);
					return "back-end/login";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = { "/hien-thi-loi-neu-khong-co-quen" }, method = RequestMethod.GET)
	public String accessDenied(final ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		return "Access denied";
	}
}
