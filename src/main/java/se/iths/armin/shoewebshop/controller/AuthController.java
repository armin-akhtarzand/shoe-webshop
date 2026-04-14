package se.iths.armin.shoewebshop.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import se.iths.armin.shoewebshop.service.AppUserService;
import se.iths.armin.shoewebshop.dto.UserRegistrationDto;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AuthController {

    private final AppUserService userService;

    public AuthController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/ott/generate-ui")
    public String ott() {
        return "ott-login";
    }

    @GetMapping("/ott/sent")
    public String ottSent() {
        return "ott-sent";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("username", userDetails.getUsername());
        return "profile";
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails, jakarta.servlet.http.HttpServletRequest request) throws jakarta.servlet.ServletException {
        userService.deleteUser(userDetails.getUsername());
        request.logout();
        return "redirect:/login?deleted";
    }

    @PostMapping("/profile/export")
    public String exportData(@AuthenticationPrincipal UserDetails userDetails) {
        userService.sendUserData(userDetails.getUsername());
        return "redirect:/profile?exported";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        userService.registerUser(userDto);
        return "redirect:/login?registered";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
