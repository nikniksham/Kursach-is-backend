package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.DTO.LogsApplicationsDTO;
import nikolausus.sem5.kursachisbackend.Mapper.LogsApplicationsMapper;
import nikolausus.sem5.kursachisbackend.entity.LogsApplications;
import nikolausus.sem5.kursachisbackend.repository.ApplicationsRepository;
import nikolausus.sem5.kursachisbackend.repository.LogsApplicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<LogsApplicationsDTO> getAllLogsByApplicationsId(Long application_id) {
        return logsApplicationsRepository.getLogsApplicationsByApplications(applicationsRepository.findById(application_id).orElseThrow(()->new RuntimeException("Соси"))).stream().map(LogsApplicationsMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<LogsApplications> getLogById(Long id) {
        return logsApplicationsRepository.findById(id);
    }
}
