package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
