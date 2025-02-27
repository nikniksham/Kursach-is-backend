package nikolausus.sem5.kursachisbackend.Mapper;

import nikolausus.sem5.kursachisbackend.DTO.ApplicationsDTO;
import nikolausus.sem5.kursachisbackend.entity.Applications;

public class ApplicationsMapper {
    public static ApplicationsDTO toDTO(Applications applications) {
        return new ApplicationsDTO(
                applications.getId(),
                UserMapper.toDTO(applications.getUser()),
                RoleMapper.toDTO(applications.getRoles()),
                StatusApplicationsMapper.toDTO(applications.getStatusApplications()),
                applications.getText()
        );
    }

    public static Applications toEntity(ApplicationsDTO applicationsDTO) {
        Applications applications = new Applications();
        applications.setId(applicationsDTO.getId());
        applications.setUser(UserMapper.toEntity(applicationsDTO.getUserDTO()));
        applications.setRoles(RoleMapper.toEntity(applicationsDTO.getRoleDTO()));
        applications.setStatusApplications(StatusApplicationsMapper.toEntity(applicationsDTO.getStatusApplicationsDTO()));
        applications.setText(applicationsDTO.getText());
        return applications;
    }
}
