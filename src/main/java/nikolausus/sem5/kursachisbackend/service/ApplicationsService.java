package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.DTO.ApplicationsDTO;
import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.entity.Role;
import nikolausus.sem5.kursachisbackend.entity.StatusApplications;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.ApplicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationsService {
    private final ApplicationsRepository applicationsRepository;
    private final LogsApplicationsService logsApplicationsRepository;
    private final StatusApplicationsService statusApplicationsService;

    public ApplicationsService(ApplicationsRepository applicationsRepository, LogsApplicationsService logsApplicationsService, StatusApplicationsService statusApplicationsService) {
        this.applicationsRepository = applicationsRepository;
        this.logsApplicationsRepository = logsApplicationsService;
        this.statusApplicationsService = statusApplicationsService;
    }

    public List<Applications> getAllApplications() {
        return applicationsRepository.findAll();
    }

    public List<Applications> getAllApplicationsByUserId(Long user_id) {
        return applicationsRepository.getAllByUserId(user_id);
    }

    public boolean checkApplicationDoesntExists(Long user_id, Role role) {
        for (Applications appl : this.getAllApplicationsByUserId(user_id)) {
            if (appl.getRoles().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public Optional<Applications> getApplicationsById(Long id) {
        return applicationsRepository.findById(id);
    }

    public boolean saveApplications(Applications applications) {
        applicationsRepository.save(applications);
        return true;
    }

    public Applications createApplication(User user, Role role , String text) {
        Applications new_application = new Applications();
        new_application.setText(text);
        new_application.setRoles(role);
        new_application.setUser(user);
        new_application.setStatusApplications(statusApplicationsService.getStatusApplicationsById(1L).orElseThrow(() -> new RuntimeException("Лог не найден")));
        return applicationsRepository.save(new_application);
    }

    public String updateApplication(Long application_id, User user, String pochemy) {
        Applications applications = this.getApplicationsById(application_id).orElseThrow(() -> new RuntimeException("Заявка не найдена"));
        if (!applications.getUser().equals(user)) {
            return "Пользователь не владелец заявки";
        }
        applications.setText(pochemy);
        applications.setStatusApplications(statusApplicationsService.getStatusApplicationsById(1L).orElseThrow(() -> new RuntimeException("Лог не найден")));
        applicationsRepository.save(applications);
        logsApplicationsRepository.createLog(user.getId(), application_id);
        return "Всё прошло успешно";
    }

    public boolean deleteApplicationsById(Long id) {
        applicationsRepository.deleteById(id);
        return true;
    }
}
