package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.Role;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.RoleRepository;
import nikolausus.sem5.kursachisbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public boolean saveUser(User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
