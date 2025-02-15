package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.repository.ApplicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationsService {
    private final ApplicationsRepository applicationsRepository;

    public ApplicationsService(ApplicationsRepository applicationsRepository) {
        this.applicationsRepository = applicationsRepository;
    }

    public List<Applications> getAllApplications() {
        return applicationsRepository.findAll();
    }

    public Optional<Applications> getApplicationsById(Long id) {
        return applicationsRepository.findById(id);
    }

    public boolean saveApplications(Applications applications) {
        applicationsRepository.save(applications);
        return true;
    }

    public boolean deleteApplicationsById(Long id) {
        applicationsRepository.deleteById(id);
        return true;
    }
}
