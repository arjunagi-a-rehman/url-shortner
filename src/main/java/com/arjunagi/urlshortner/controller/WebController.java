package com.arjunagi.urlshortner.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

  @Value("${kalrav.api.key:}")
  private String kalravApiKey;

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("kalravApiKey", kalravApiKey);
    return "index";
  }

  @GetMapping("/home")
  public String home() {
    return "redirect:/";
  }

  // Handle SPA routing - redirect all non-API routes to index.html
  @RequestMapping(value = { "/features", "/analytics", "/about" })
  public String spa(Model model) {
    model.addAttribute("kalravApiKey", kalravApiKey);
    return "index";
  }
}