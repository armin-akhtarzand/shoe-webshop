package se.iths.armin.shoewebshop.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.iths.armin.shoewebshop.dto.UserRegistrationDto;
import se.iths.armin.shoewebshop.entity.AppUser;
import se.iths.armin.shoewebshop.repository.AppUserRepository;

@Service
public class AppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserRegistrationDto userDto) {
        AppUser user = new AppUser();
        user.setUsername(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setConsent(userDto.isConsent());
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }
}
