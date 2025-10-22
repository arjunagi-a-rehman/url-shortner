package com.arjunagi.urlshortner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

    // Handle SPA routing - redirect all non-API routes to index.html
    @RequestMapping(value = {"/features", "/analytics", "/about"})
    public String spa() {
        return "forward:/index.html";
    }
}