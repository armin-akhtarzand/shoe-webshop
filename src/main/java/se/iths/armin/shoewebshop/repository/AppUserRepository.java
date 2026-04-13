package se.iths.armin.shoewebshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.armin.shoewebshop.entity.AppUser;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
