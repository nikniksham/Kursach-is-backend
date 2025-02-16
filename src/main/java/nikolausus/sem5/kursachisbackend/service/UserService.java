package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.Role;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.RoleRepository;
import nikolausus.sem5.kursachisbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String login, String rawPassword) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new RuntimeException("Логин уже занят");
        }

        Role simpleRole = roleRepository.findByName("simple")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("simple");
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(rawPassword)); // 👈 Хешируем пароль!
        // Добавляем роль "simple" пользователю
        Set<Role> roles = new HashSet<>();
        roles.add(simpleRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        // 1. Найти пользователя и роль в базе
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Роль не найдена"));

        // 2. Добавить роль в коллекцию ролей пользователя
        user.getRoles().add(role);

        // 3. Сохранить пользователя (с ролями)
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
