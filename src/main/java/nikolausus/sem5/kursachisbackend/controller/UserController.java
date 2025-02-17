package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping
    public Optional<User> getUserById(@RequestParam Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/hello")
    public String userHello() {
        System.out.println("Запрос к юзеру");
        return "Hello, User!";
    }
}
