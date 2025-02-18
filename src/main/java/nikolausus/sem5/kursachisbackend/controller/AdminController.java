package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.service.ApplicationsService;
import nikolausus.sem5.kursachisbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final ApplicationsService applicationsService;

    public AdminController(UserService userService, ApplicationsService applicationsService) {
        this.userService = userService;
        this.applicationsService = applicationsService;
    }

    @GetMapping("/user/assignRole")
    public ResponseEntity<String> assignRoleToUser(@RequestParam Long userId, @RequestParam Long roleId) {
        try {
            userService.assignRoleToUser(userId, roleId);
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
        return ResponseEntity.ok("Роль успешно добавлена");
    }

    @GetMapping("/user/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/user/delete")
    public void deleteUser(@RequestParam Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/applications/create")
    public boolean createApplications(@RequestBody Applications applications) {
        return applicationsService.saveApplications(applications);
    }

    @DeleteMapping("/applications/delete")
    public boolean deleteApplicationsById(@RequestParam Long id) {
        return applicationsService.deleteApplicationsById(id);
    }

    @GetMapping("/hello")
    public String adminHello() {
//        System.out.println("Запрос к админу");
        return "Hello, Admin!";
    }
}
