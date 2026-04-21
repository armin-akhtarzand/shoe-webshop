package se.iths.armin.shoewebshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyController {

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy-policy";
    }

    @GetMapping("/cookies")
    public String cookies() {
        return "cookie-policy";
    }
}