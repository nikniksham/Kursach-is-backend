package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.DTO.ApplicationsDTO;
import nikolausus.sem5.kursachisbackend.DTO.RoleDTO;
import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
import nikolausus.sem5.kursachisbackend.Mapper.ApplicationsMapper;
import nikolausus.sem5.kursachisbackend.Mapper.RoleMapper;
import nikolausus.sem5.kursachisbackend.Mapper.StatusApplicationsMapper;
import nikolausus.sem5.kursachisbackend.Mapper.UserMapper;
import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.entity.Role;
import nikolausus.sem5.kursachisbackend.entity.StatusApplications;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.ApplicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<ApplicationsDTO> getAllApplications() {
        return applicationsRepository.findAll().stream().map(ApplicationsMapper::toDTO).collect(Collectors.toList());
    }

    public List<ApplicationsDTO> getAllApplicationsByUserId(Long user_id) {
        return applicationsRepository.getAllByUserId(user_id).stream().map(ApplicationsMapper::toDTO).collect(Collectors.toList());
    }

    public boolean checkApplicationDoesntExists(Long user_id, RoleDTO roleDTO) {
        for (ApplicationsDTO appl : this.getAllApplicationsByUserId(user_id)) {
            if (appl.getRoleDTO().getId().equals(RoleMapper.toEntity(roleDTO).getId())) {
                return true;
            }
        }

        return false;
    }

    public ApplicationsDTO getApplicationsById(Long id) {
        return ApplicationsMapper.toDTO(applicationsRepository.findById(id).orElseThrow(()->new RuntimeException("Заявка с заданным id не найдена")));
    }

    public boolean saveApplications(ApplicationsDTO applicationsDTO) {
        applicationsRepository.save(ApplicationsMapper.toEntity(applicationsDTO));
        return true;
    }

    public ApplicationsDTO createApplication(UserDTO userDTO, RoleDTO roleDTO , String text) {
        Applications new_application = new Applications();
        new_application.setText(text);
        new_application.setRoles(RoleMapper.toEntity(roleDTO));
        new_application.setUser(UserMapper.toEntity(userDTO));
        new_application.setStatusApplications(StatusApplicationsMapper.toEntity(statusApplicationsService.getStatusApplicationsById(1L)));
        return ApplicationsMapper.toDTO(applicationsRepository.save(new_application));
    }

    public String updateApplication(Long application_id, UserDTO userDTO, String pochemy) {
        Applications applications = ApplicationsMapper.toEntity(this.getApplicationsById(application_id));
        if (!applications.getUser().getId().equals(UserMapper.toEntity(userDTO).getId())) {
            return "Пользователь не владелец заявки";
        }
        applications.setText(pochemy);
        applications.setStatusApplications(StatusApplicationsMapper.toEntity(statusApplicationsService.getStatusApplicationsById(1L)));
        applicationsRepository.save(applications);
        logsApplicationsRepository.createLog(userDTO.getId(), application_id);
        return "Всё прошло успешно";
    }

    public boolean deleteApplicationsById(Long id) {
        applicationsRepository.deleteById(id);
        return true;
    }
}
