package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.LogsApplications;
import nikolausus.sem5.kursachisbackend.repository.ApplicationsRepository;
import nikolausus.sem5.kursachisbackend.repository.LogsApplicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogsApplicationsService {
    private final LogsApplicationsRepository logsApplicationsRepository;
    private final ApplicationsRepository applicationsRepository;

    public LogsApplicationsService(LogsApplicationsRepository logsApplicationsRepository, ApplicationsRepository applicationsService) {
        this.logsApplicationsRepository = logsApplicationsRepository;
        this.applicationsRepository = applicationsService;
    }

    public void createLog(Long whoId, Long applicationId) {
        logsApplicationsRepository.createLog(whoId, applicationId);
    }

    public List<LogsApplications> getAllLogs() {
        return logsApplicationsRepository.findAll();
    }

    public List<LogsApplications> getAllLogsByApplicationsId(Long application_id) {
        return logsApplicationsRepository.getLogsApplicationsByApplications(applicationsRepository.findById(application_id).orElseThrow(()->new RuntimeException("Соси")));
    }

    public Optional<LogsApplications> getLogById(Long id) {
        return logsApplicationsRepository.findById(id);
    }
}
