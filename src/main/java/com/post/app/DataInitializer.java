package com.post.app;

import com.post.app.domain.ERole;
import com.post.app.domain.Role;
import com.post.app.domain.User;
import com.post.app.repositories.RoleRepository;
import com.post.app.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

// for development purpose
@Configuration
public class DataInitializer {

    private static Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Profile("init-role")
    @Bean
    public CommandLineRunner roleInitializer(RoleRepository roleRepo) {
        return args -> {
            roleRepo.save(new Role(ERole.ROLE_USER));
            roleRepo.save(new Role(ERole.ROLE_MODERATOR));
            roleRepo.save(new Role(ERole.ROLE_ADMIN));
        };
    }

    @Profile("init-user")
    @Bean
    public CommandLineRunner userInitializer(UserRepository userRepo,
                                             RoleRepository roleRepo,
                                             PasswordEncoder encoder) {
        return args -> {
            Optional<Role> role_user = roleRepo.findByName(ERole.ROLE_USER);
            if (role_user.isEmpty()) {
                throw new RuntimeException("Role not found");
            }
            User user = new User("John",
                    encoder.encode("12345"),
                    "John@email.com",
                    Set.of(role_user.get()));
            userRepo.save(user);

            log.info("'" + user.getUsername() + "' is saved");
        };
    }
}
