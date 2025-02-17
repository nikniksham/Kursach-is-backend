package nikolausus.sem5.kursachisbackend.controller;

import lombok.Getter;
import lombok.Setter;
import nikolausus.sem5.kursachisbackend.entity.Role;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.jwt.JwtUtil;
import nikolausus.sem5.kursachisbackend.repository.RoleRepository;
import nikolausus.sem5.kursachisbackend.repository.UserRepository;
import nikolausus.sem5.kursachisbackend.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new RuntimeException("Логин занят");
        }
        Role userRole = roleRepository.findByName("ROLE_SIMPLE").orElseThrow(() -> new RuntimeException("Не смог найти роль simple"));
        user.setRoles(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "Успешная регистрация!";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Неверный логин или пароль", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getLogin());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }
}

@Getter
@Setter
class AuthRequest {
    private String login;
    private String password;
}