package se.iths.armin.shoewebshop.security;

import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;
import se.iths.armin.mailservice.MailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EmailOttSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    private final MailService mailService;

    public EmailOttSuccessHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken token) throws IOException {
        String loginLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/login/ott?token=" + token.getTokenValue();
        
        String subject = "Your Login Link for Shoe Webshop";
        String message = "Click the following link to login: " + loginLink + "\nThe link is valid for 5 minutes.";

        try {
            mailService.sendMail(token.getUsername(), subject, message);
        } catch (Exception e) {
            // Log error but don't crash the authentication flow
            System.err.println("Failed to send OTT email to " + token.getUsername() + ": " + e.getMessage());
        }

        response.sendRedirect("/ott/sent");
    }
}
