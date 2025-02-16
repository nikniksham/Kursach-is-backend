package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.entity.LogsApplications;
import nikolausus.sem5.kursachisbackend.service.LogsApplicationsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/logsApplications")
public class LogsApplicationsController {
    private final LogsApplicationsService logsApplicationsService;

    public LogsApplicationsController(LogsApplicationsService logsApplicationsService) {
        this.logsApplicationsService = logsApplicationsService;
    }

    @GetMapping("/all")
    public List<LogsApplications> getAllLogs() {
        return logsApplicationsService.getAllLogs();
    }

    @GetMapping
    public Optional<LogsApplications> getLogById(@RequestParam Long id) {
        return logsApplicationsService.getLogById(id);
    }

    @PostMapping("/createLog")
    public void createLog(@RequestParam Long adminId, @RequestParam Long applicationId) {
        logsApplicationsService.createLog(adminId, applicationId);
    }
}
