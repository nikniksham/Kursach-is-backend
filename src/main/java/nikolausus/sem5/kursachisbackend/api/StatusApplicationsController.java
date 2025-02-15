package nikolausus.sem5.kursachisbackend.api;

import nikolausus.sem5.kursachisbackend.entity.StatusApplications;
import nikolausus.sem5.kursachisbackend.service.StatusApplicationsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/status_applications")
public class StatusApplicationsController {
    private final StatusApplicationsService statusApplicationsService;

    public StatusApplicationsController(StatusApplicationsService statusApplicationsService) {
        this.statusApplicationsService = statusApplicationsService;
    }

    @GetMapping("/all")
    public List<StatusApplications> getAllStatusApplications() {
        return statusApplicationsService.getAllStatusApplications();
    }

    @GetMapping
    public Optional<StatusApplications> getStatusApplicationsById(@RequestParam Long id) {
        return statusApplicationsService.getStatusApplicationsById(id);
    }
}
