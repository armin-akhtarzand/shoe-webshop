package se.iths.armin.shoewebshop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class CookieController {

    @PostMapping("/cookies/accept")
    public String acceptCookies(HttpServletResponse response,
                                @RequestHeader(value = "Referer", required = false) String referer) {

        Cookie cookie = new Cookie("cookieConsent", "accepted");
        cookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/cookies/decline")
    public String declineCookies(HttpServletResponse response,
                                 @RequestHeader(value = "Referer", required = false) String referer) {

        Cookie cookie = new Cookie("cookieConsent", "declined");
        cookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:" + (referer != null ? referer : "/");
    }
}