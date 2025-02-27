package nikolausus.sem5.kursachisbackend.controller;

import lombok.Getter;
import lombok.Setter;
import nikolausus.sem5.kursachisbackend.DTO.RoleDTO;
import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
import nikolausus.sem5.kursachisbackend.jwt.JwtUtil;
import nikolausus.sem5.kursachisbackend.repository.UserRepository;
import nikolausus.sem5.kursachisbackend.service.RoleService;
import nikolausus.sem5.kursachisbackend.service.UserDetailsServiceImpl;
import nikolausus.sem5.kursachisbackend.service.UserService;
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

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody AuthRequest authRequest) {
        if (userService.existsByLogin(authRequest.getLogin())) {
            throw new RuntimeException("Логин занят");
        }
        boolean dontHaveAnyAdmin = true;
        RoleDTO admin_role = roleService.getRoleByName("ROLE_ADMIN");
        for (UserDTO us : userService.getAllUsers()) {
            for (RoleDTO ro : us.getRolesDTO()) {
                if (ro.getId().equals(admin_role.getId())) {
                    dontHaveAnyAdmin = false;
                    break;
                }
            }
        }
        Set<RoleDTO> roles = new HashSet<>();
        if (dontHaveAnyAdmin) {
            roles.add(admin_role);
        }
        RoleDTO userRole = roleService.getRoleByName("ROLE_SIMPLE");
        roles.add(userRole);
        try {
            UserDTO userDTO = userService.createNewUser(authRequest.getLogin(), passwordEncoder.encode(authRequest.getPassword()), roles);
            return "Успешная регистрация!";
        } catch (Exception e) {
            return e.getMessage();
        }
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