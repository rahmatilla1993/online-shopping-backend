package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.LoginDto;
import com.example.onlineshopping.dto.RegisterDto;
import com.example.onlineshopping.entity.Role;
import com.example.onlineshopping.entity.User;
import com.example.onlineshopping.enums.RoleName;
import com.example.onlineshopping.enums.Status;
import com.example.onlineshopping.exception.UserExistException;
import com.example.onlineshopping.repository.RoleRepository;
import com.example.onlineshopping.repository.UserRepository;
import com.example.onlineshopping.response.ApiResponse;
import com.example.onlineshopping.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
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
        throw new UserExistException("This username already exists");
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
}
