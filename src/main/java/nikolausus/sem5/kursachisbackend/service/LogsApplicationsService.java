package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.LogsApplications;
import nikolausus.sem5.kursachisbackend.repository.LogsApplicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogsApplicationsService {
    private final LogsApplicationsRepository logsApplicationsRepository;

    public LogsApplicationsService(LogsApplicationsRepository logsApplicationsRepository) {
        this.logsApplicationsRepository = logsApplicationsRepository;
    }

    public void createLog(Long adminId, Long applicationId) {
        logsApplicationsRepository.createLog(adminId, applicationId);
    }

    public List<LogsApplications> getAllLogs() {
        return logsApplicationsRepository.findAll();
    }

    public Optional<LogsApplications> getLogById(Long id) {
        return logsApplicationsRepository.findById(id);
    }
}
