package com.post.app.repositories;

import com.post.app.domain.ERole;
import com.post.app.domain.Role;
import com.post.app.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql({"/db/init_role.sql", "/db/init_user.sql"})
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    void findByUsername() {
        User user = userRepository.findByUsername("John").get();

        assertThat(user).isNotNull();
    }

    @Test
    void existsByUsername() {
        Boolean bool = userRepository.existsByUsername("John");

        assertThat(bool).isTrue();
    }

    @Test
    void save() {
        Role role_user = roleRepository.findByName(ERole.ROLE_USER).get();
        User user = new User("John", "12345", "a@b.com", Set.of(role_user));

        User saved_user = userRepository.save(user);

        assertThat(saved_user).isNotNull();
        assertThat(saved_user.getUsername()).isEqualTo("John");
    }
}