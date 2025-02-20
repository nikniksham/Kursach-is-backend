package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.entity.CommentOnApplications;
import nikolausus.sem5.kursachisbackend.entity.LogsApplications;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.jwt.JwtUtil;
import nikolausus.sem5.kursachisbackend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final ApplicationsService applicationsService;
    private final LogsApplicationsService logsApplicationsService;
    private final StatusApplicationsService statusApplicationsService;
    private final JwtUtil jwtUtil;
    private final CommentOnApplicationsService commentOnApplicationsService;

    public AdminController(UserService userService, ApplicationsService applicationsService, LogsApplicationsService logsApplicationsService,
                           StatusApplicationsService statusApplicationsService, JwtUtil jwtUtil, CommentOnApplicationsService commentOnApplicationsService) {
        this.userService = userService;
        this.applicationsService = applicationsService;
        this.logsApplicationsService = logsApplicationsService;
        this.statusApplicationsService = statusApplicationsService;
        this.jwtUtil = jwtUtil;
        this.commentOnApplicationsService = commentOnApplicationsService;
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

    @GetMapping("/application/all")
    public List<Applications> getAllApplications() {
        return applicationsService.getAllApplications();
    }

    @GetMapping("/application/getAllLogs")
    public List<LogsApplications> getAllApplications(@RequestParam Long application_id) {
        return logsApplicationsService.getAllLogsByApplicationsId(application_id);
    }

    @GetMapping("/application/getById")
    public Applications getApplicationById(@RequestParam Long application_id) {
        return applicationsService.getApplicationsById(application_id).orElseThrow(() -> new RuntimeException("Нету"));
    }

    @PostMapping("/application/reject")
    public String rejectApplication(@RequestParam Long application_id, @RequestParam String why_not, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.substring(7);
        User user = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Long user_id = user.getId();
        Applications applications = applicationsService.getApplicationsById(application_id).orElseThrow(() -> new RuntimeException("Нету"));
        applications.setStatusApplications(statusApplicationsService.getStatusApplicationsById(3L).orElseThrow());
        applicationsService.saveApplications(applications);
        logsApplicationsService.createLog(user_id, application_id);

        Optional<CommentOnApplications> comment = commentOnApplicationsService.getCommentByApplicationId(application_id);
        if (comment.isPresent()) {
            commentOnApplicationsService.editCommentOnApplication(why_not, comment.get().getId());
        } else {
            commentOnApplicationsService.createCommentOnApplication(application_id, user, why_not);
        }
        return "Заявка отклонена";
    }

    @PostMapping("/application/approve")
    public String approveApplication(@RequestParam Long application_id, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.substring(7);
        Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();
        Applications applications = applicationsService.getApplicationsById(application_id).orElseThrow(() -> new RuntimeException("Нету"));
        applications.setStatusApplications(statusApplicationsService.getStatusApplicationsById(2L).orElseThrow());
        applicationsService.saveApplications(applications);
        logsApplicationsService.createLog(user_id, application_id);
        userService.assignRoleToUser(applications.getUser().getId(), applications.getRoles().getId());
        return "Заявка одобрена";
    }

    @GetMapping("/hello")
    public String adminHello() {
//        System.out.println("Запрос к админу");
        return "Hello, Admin!";
    }
}
