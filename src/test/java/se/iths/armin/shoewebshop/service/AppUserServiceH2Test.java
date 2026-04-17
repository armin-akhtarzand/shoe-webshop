package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import se.iths.armin.mailservice.MailService;
import se.iths.armin.shoewebshop.dto.UserRegistrationDto;
import se.iths.armin.shoewebshop.entity.AppUser;
import se.iths.armin.shoewebshop.repository.AppUserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
class AppUserServiceH2Test {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository userRepository;

    @MockitoBean
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser_ShouldPersistUserInH2() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("database-test@example.com");
        dto.setPassword("password123");
        dto.setConsent(true);

        appUserService.registerUser(dto);

        Optional<AppUser> found = userRepository.findByUsername("database-test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("database-test@example.com");
        assertThat(passwordEncoder.matches("password123", found.get().getPassword())).isTrue();
    }

    @Test
    void deleteUser_ShouldRemoveUserFromH2() {
        AppUser user = new AppUser();
        user.setUsername("delete-me@example.com");
        user.setPassword("secret");
        userRepository.save(user);
        assertThat(userRepository.findByUsername("delete-me@example.com")).isPresent();

        appUserService.deleteUser("delete-me@example.com");

        assertThat(userRepository.findByUsername("delete-me@example.com")).isEmpty();
    }

    @Test
    void sendUserData_ShouldInteractWithDatabaseAndMailService() {
        AppUser user = new AppUser();
        user.setUsername("export@example.com");
        user.setPassword("secret");
        user.setRole("ROLE_USER");
        user.setConsent(true);
        userRepository.save(user);

        appUserService.sendUserData("export@example.com");

        verify(mailService).sendMail(eq("export@example.com"), anyString(), anyString());
    }
}
