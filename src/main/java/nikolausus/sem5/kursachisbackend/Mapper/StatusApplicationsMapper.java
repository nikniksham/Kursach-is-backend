package nikolausus.sem5.kursachisbackend.Mapper;

import nikolausus.sem5.kursachisbackend.DTO.StatusApplicationsDTO;
import nikolausus.sem5.kursachisbackend.entity.StatusApplications;

public class StatusApplicationsMapper {
    public static StatusApplicationsDTO toDTO(StatusApplications statusApplications) {
        return new StatusApplicationsDTO(
                statusApplications.getId(),
                statusApplications.getStatus());
    }

    public static StatusApplications toEntity(StatusApplicationsDTO statusApplicationsDTO) {
        StatusApplications statusApplications = new StatusApplications();
        statusApplications.setId(statusApplicationsDTO.getId());
        statusApplications.setStatus(statusApplicationsDTO.getStatus());
        return statusApplications;
    }
}
