package com.night.filter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/page")
@Controller
public class PageController {



    @RequestMapping("/secret")
    public String secretPage(){

        return "secret";
    }
}
