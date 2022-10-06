package com.luxuryshop.controller.admin;

import com.luxuryshop.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class AdminContactController {

    @Autowired
    ContactRepository contactRepo;

    @RequestMapping(value = {"/admin/contacts"}, method = RequestMethod.GET)
    public String index(final ModelMap model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        model.addAttribute("contacts", contactRepo.findAllByOrderByIdDesc());
        return "back-end/view_contacts";
    }
}
