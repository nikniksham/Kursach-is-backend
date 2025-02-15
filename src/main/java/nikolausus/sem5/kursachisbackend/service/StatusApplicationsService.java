package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.StatusApplications;
import nikolausus.sem5.kursachisbackend.repository.StatusApplicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusApplicationsService {
    private final StatusApplicationsRepository statusApplicationsRepository;

    public StatusApplicationsService(StatusApplicationsRepository statusApplicationsRepository) {
        this.statusApplicationsRepository = statusApplicationsRepository;
    }

    public List<StatusApplications> getAllStatusApplications() {
        return statusApplicationsRepository.findAll();
    }

    public Optional<StatusApplications> getStatusApplicationsById(Long id) {
        return statusApplicationsRepository.findById(id);
    }
}
