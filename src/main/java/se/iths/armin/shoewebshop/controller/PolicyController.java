package se.iths.armin.shoewebshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyController {

    @GetMapping("/privacy-policy")
    public String privacy() {
        return "privacy-policy";
    }

    @GetMapping("/cookie-policy")
    public String cookies() {
        return "cookie-policy";
    }
}