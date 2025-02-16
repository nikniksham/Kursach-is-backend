package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.service.ApplicationsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class ApplicationsController {
    private final ApplicationsService applicationsService;

    private ApplicationsController(ApplicationsService applicationsService) {
        this.applicationsService = applicationsService;
    }

    @GetMapping("/all")
    public List<Applications> getAllApplications() {
        return applicationsService.getAllApplications();
    }

    @GetMapping
    public Optional<Applications> getApplicationsById(@RequestParam Long id) {
        return applicationsService.getApplicationsById(id);
    }
}
