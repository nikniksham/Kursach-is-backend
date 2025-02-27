package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.DTO.RoleDTO;
import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
import nikolausus.sem5.kursachisbackend.Mapper.RoleMapper;
import nikolausus.sem5.kursachisbackend.Mapper.UserMapper;
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
import java.util.stream.Collectors;

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

    public UserDTO getUserByLogin(String login) {
        return UserMapper.toDTO(userRepository.findByLogin(login).orElseThrow(()->new RuntimeException("Пользователь с заданным login не найден")));
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

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return UserMapper.toDTO(userRepository.findById(id).orElseThrow(()->new RuntimeException("Пользователь с указанынм id не найден")));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public UserDTO createNewUser(String login, String password, Set<RoleDTO> roles) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRoles(roles.stream().map(RoleMapper::toEntity).collect(Collectors.toSet()));
        return UserMapper.toDTO(userRepository.save(user));
    }
}
