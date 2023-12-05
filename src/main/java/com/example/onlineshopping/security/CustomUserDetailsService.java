package com.example.onlineshopping.security;

import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.enums.Status;
import com.example.onlineshopping.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository
                .findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .accountExpired(false)
                    .accountLocked(user.getStatus().equals(Status.BANNED))
                    .authorities(Collections
                            .singletonList(
                                    new SimpleGrantedAuthority(
                                            user
                                                    .getRole()
                                                    .getRoleName()
                                                    .name()
                                    )))
                    .build();
        }
        log.error("User with %s username not found!".formatted(username));
        throw new UsernameNotFoundException("User not found");
    }
}
