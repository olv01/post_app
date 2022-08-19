package com.post.app.security;

import com.post.app.domain.User;
import com.post.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // User entity implements UserDetails, So we just return the User object
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("User '" + username + "' not found");
        return user.get();
    }
}
