package se.iths.armin.shoewebshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.iths.armin.mailservice.MailService;
import se.iths.armin.shoewebshop.dto.UserRegistrationDto;
import se.iths.armin.shoewebshop.entity.AppUser;
import se.iths.armin.shoewebshop.repository.AppUserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceMockTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        appUserService = new AppUserService(userRepository, passwordEncoder, mailService);
    }

    @Test
    void registerUser_ShouldEncodePasswordAndSaveUser() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test@example.com");
        dto.setPassword("rawPassword");
        dto.setConsent(true);

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        appUserService.registerUser(dto);

        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).save(userCaptor.capture());

        AppUser savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("test@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRole()).isEqualTo("ROLE_USER");
        assertThat(savedUser.isConsent()).isTrue();
    }

    @Test
    void deleteUser_ShouldCallDeleteIfUserExists() {
        AppUser user = new AppUser();
        user.setUsername("test@example.com");
        when(userRepository.findByUsername("test@example.com")).thenReturn(Optional.of(user));

        appUserService.deleteUser("test@example.com");

        verify(userRepository).delete(user);
    }

    @Test
    void sendUserData_ShouldSendMailIfUserExists() {
        AppUser user = new AppUser();
        user.setUsername("test@example.com");
        user.setRole("ROLE_USER");
        user.setConsent(true);
        when(userRepository.findByUsername("test@example.com")).thenReturn(Optional.of(user));

        appUserService.sendUserData("test@example.com");

        verify(mailService).sendMail(eq("test@example.com"), anyString(), contains("test@example.com"));
    }
}
