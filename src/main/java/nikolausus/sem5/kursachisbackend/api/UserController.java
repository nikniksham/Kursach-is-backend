package nikolausus.sem5.kursachisbackend.api;

import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/id/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/login/{login}")
    public User getUserById(@PathVariable String login) {
        return userService.getUserByLogin(login);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        System.out.println(user.getLogin());
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
