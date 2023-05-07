package com.luxuryshop.controller.admin;

import com.luxuryshop.entities.Discount;
import com.luxuryshop.repositories.DiscountRepository;
import com.luxuryshop.services.MyUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AdminDiscountController {

    @Autowired
    DiscountRepository discountRepository;

    @RequestMapping(value = {"/admin/discounts"}, method = RequestMethod.GET)
    public String index(final ModelMap model, final HttpServletRequest request, final HttpServletResponse Response) throws Exception {
        List<Discount> discounts = discountRepository.findAll();
        model.addAttribute("discounts", discounts);
        return "back-end/view_discounts";
    }

    @RequestMapping(value = {"/admin/discount-add"}, method = RequestMethod.GET)
    public String viewadd(final ModelMap model, final HttpServletRequest request, final HttpServletResponse Response) throws Exception {
        Discount discount = null;
        String id = request.getParameter("id");
        if (id == null) discount = new Discount();
        else discount = discountRepository.getById(Integer.valueOf(id));
        model.addAttribute("mgg", discount);
        return "back-end/insert_discount";
    }

    @Transactional
    @RequestMapping(value = {"/admin/discount-remove/{id}"}, method = RequestMethod.GET)
    public String remove(@PathVariable Integer id, final ModelMap model, final HttpServletRequest request, final HttpServletResponse Response) throws Exception {
        discountRepository.deleteById(id);
        return "redirect:/admin/discounts";
    }

    @Transactional
    @RequestMapping(value = {"/admin/discount-add"}, method = RequestMethod.POST)
    public String add(@ModelAttribute Discount mgg, final ModelMap model, final HttpServletRequest request, final HttpServletResponse Response) throws Exception {
        String discount = request.getParameter("num_discount");
        mgg.setDiscount(Float.parseFloat(discount));
        MyUserDetail userDetail = (MyUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Discount newDiscount = null;
        if (mgg.getId() != null) {
            newDiscount = discountRepository.getById(mgg.getId());
            newDiscount.setName(mgg.getName());
            newDiscount.setDiscount(mgg.getDiscount());
            newDiscount.setRemain(mgg.getRemain());
            newDiscount.setUpdatedDate(LocalDateTime.now());
            newDiscount.setUpdatedBy(userDetail.getUser().getId());
            discountRepository.save(newDiscount);
        } else {
            mgg.setCreatedBy(userDetail.getUser().getId());
            mgg.setCreatedDate(LocalDateTime.now());
            discountRepository.save(mgg);
        }

        return "redirect:/admin/discounts";
    }
}
