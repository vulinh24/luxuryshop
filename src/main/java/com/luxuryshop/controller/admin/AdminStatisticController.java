package com.luxuryshop.controller.admin;

import com.luxuryshop.entities.Role;
import com.luxuryshop.services.Chart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminStatisticController {
    @Autowired
    Chart chart;

    @RequestMapping(value = { "/super-admin/statistic" }, method = RequestMethod.GET)
    public String index(final ModelMap model, final HttpServletRequest request, final HttpServletResponse Response)
            throws Exception {
        return "back-end/statistic";
    }

    @ResponseBody
    @RequestMapping(value = "/caculate", method = RequestMethod.GET)
    public ResponseEntity cacul() {
        return ResponseEntity.ok(chart.count());
    }
}
