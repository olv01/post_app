package com.post.app.repositories;

import com.post.app.domain.ERole;
import com.post.app.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    @Sql({"/db/init_role.sql"})
    void findByName() {
        Role role_user = roleRepository.findByName(ERole.ROLE_USER).get();

        assertThat(role_user.getName()).isEqualTo(ERole.ROLE_USER);
    }
}