package nikolausus.sem5.kursachisbackend.api;

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

    @PostMapping
    public boolean createApplications(@RequestBody Applications applications) {
        return applicationsService.saveApplications(applications);
    }

    @DeleteMapping
    public boolean deleteApplicationsById(@RequestParam Long id) {
        return applicationsService.deleteApplicationsById(id);
    }
}
