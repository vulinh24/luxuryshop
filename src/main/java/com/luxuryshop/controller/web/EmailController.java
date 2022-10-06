package com.luxuryshop.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxuryshop.entities.Contact;
import com.luxuryshop.model.NotificationEmail;
import com.luxuryshop.repositories.ContactRepository;
import com.luxuryshop.services.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class EmailController {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    SendMailService mailService;

    @Autowired
    SendMailService sendMail;

    @ResponseBody
    @RequestMapping(value = {"/save-notification-email"}, method = RequestMethod.POST)
    public void saveContactWithAjax(final ModelMap model, final HttpServletRequest request, final HttpServletResponse response,
                                    @RequestBody NotificationEmail data) throws Exception {
        ObjectMapper om = new ObjectMapper();
        Contact contact = new Contact();
        contact.setEmail(data.getEmail());
        contactRepository.save(contact);
        om.writeValue(response.getOutputStream(), data);
    }
}
