package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.LoginDto;
import com.example.onlineshopping.dto.RegisterDto;
import com.example.onlineshopping.entity.Role;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.enums.RoleName;
import com.example.onlineshopping.enums.Status;
import com.example.onlineshopping.exception.UserAlreadyExistsException;
import com.example.onlineshopping.facade.UserFacade;
import com.example.onlineshopping.repository.RoleRepository;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.response.ApiResponse;
import com.example.onlineshopping.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    private Role findByRoleName(RoleName roleName) {
        return roleRepository
                .findByRoleName(roleName)
                .orElse(null);
    }


    private boolean isNotExistsUsername(String username) {
        Optional<User> optionalUser = userRepository
                .findByUsername(username);
        if (optionalUser.isEmpty())
            return true;
        throw new UserAlreadyExistsException("This username already exists");
    }

    @Transactional
    public ApiResponse create(RegisterDto registerDto) {
        String username = registerDto.getUsername();
        if (isNotExistsUsername(username)) {
            Role role = findByRoleName(RoleName.ROLE_USER);
            User user = User.builder()
                    .firstName(registerDto.getFirstName())
                    .lastName(registerDto.getLastName())
                    .username(username)
                    .role(role)
                    .status(Status.ACTIVE)
                    .password(passwordEncoder.encode(registerDto.getPassword()))
                    .build();
            userRepository.save(user);
            return new ApiResponse("User registered successfully!", true);
        }
        return null;
    }

    public Map<String, String> login(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = String.format("Bearer %s", jwtUtil.generateToken(username));
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            data.put("username", username);
            return data;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    public UserFacade getUser() {
        String username = getUsername();
        Optional<User> optionalUser = userRepository
                .findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return UserFacade.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .roles(Collections
                            .singletonList(
                                    user.getRole()
                                            .getRoleName()
                                            .name())
                    )
                    .status(user.getStatus().name())
                    .imageUrl(user.getImageUrl())
                    .build();
        }
        log.error("User with %s username not found".formatted(username));
        throw new UsernameNotFoundException("User not found");
    }

    private String getUsername() {
        return ((org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
