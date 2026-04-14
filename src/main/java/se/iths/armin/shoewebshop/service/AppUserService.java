package se.iths.armin.shoewebshop.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.iths.armin.mailservice.MailService;
import se.iths.armin.shoewebshop.dto.UserRegistrationDto;
import se.iths.armin.shoewebshop.entity.AppUser;
import se.iths.armin.shoewebshop.repository.AppUserRepository;

@Service
public class AppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public void registerUser(UserRegistrationDto userDto) {
        AppUser user = new AppUser();
        user.setUsername(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setConsent(userDto.isConsent());
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.findByUsername(username).ifPresent(userRepository::delete);
    }

    public void sendUserData(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            String subject = "Your Data Export - Shoe Webshop";
            String message = "Here is the data we have stored for your account:\n" +
                             "- Username (Email): " + user.getUsername() + "\n" +
                             "- Consent for personal data: " + (user.isConsent() ? "Yes" : "No") + "\n" +
                             "- Role: " + user.getRole();
            mailService.sendMail(user.getUsername(), subject, message);
        });
    }
}
