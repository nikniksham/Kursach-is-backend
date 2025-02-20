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

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Роль не найдена"));

        user.getRoles().add(role);

        userRepository.save(user);
    }

    public void deleteRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (roleId == 1) {
            throw new RuntimeException("Нельзя забрать базовую роль");
        }

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Роль не найдена"));

        if (!user.getRoles().contains(role)) {
            throw new RuntimeException("У пользователя нет этой роли");
        }

        user.getRoles().remove(role);

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
