package com.shiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/6 16:26
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/admin.page")
    public ModelAndView page() {
        return new ModelAndView("admin");
    }
}
