package com.luxuryshop.apis;

import com.luxuryshop.entities.Contact;
import com.luxuryshop.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AppApi {

    @Autowired
    ContactRepository contactRepository;

    @GetMapping("/reply-contact/{id}")
    public String updateContact(@PathVariable Integer id) {
        Optional<Contact> opt = contactRepository.findById(id);
        opt.get().setIsReplied(true);
        contactRepository.save(opt.get());
        return "ok";
    }
}
