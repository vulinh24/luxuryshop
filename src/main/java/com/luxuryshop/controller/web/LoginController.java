package com.luxuryshop.controller.web;

import com.luxuryshop.entities.User;
import com.luxuryshop.services.LoginFBService;
import com.luxuryshop.services.LoginGGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    LoginFBService loginFBService;

    @Autowired
    LoginGGService loginGGService;

    @GetMapping("/login")
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

    @GetMapping("/auth/facebook-login")
    public void loginFb(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        loginFBService.loginByCode(code, request, response);
    }

    @GetMapping("/auth/google-login")
    public void loginGg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        loginGGService.loginByCode(code, request, response);
    }
}
