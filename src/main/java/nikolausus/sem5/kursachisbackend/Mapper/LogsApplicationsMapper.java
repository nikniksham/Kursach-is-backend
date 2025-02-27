package nikolausus.sem5.kursachisbackend.Mapper;

import nikolausus.sem5.kursachisbackend.DTO.LogsApplicationsDTO;
import nikolausus.sem5.kursachisbackend.entity.LogsApplications;

public class LogsApplicationsMapper {
    public static LogsApplicationsDTO toDTO(LogsApplications logsApplications) {
        return new LogsApplicationsDTO(logsApplications.getId(),
                UserMapper.toDTO(logsApplications.getUser()),
                ApplicationsMapper.toDTO(logsApplications.getApplications()),
                StatusApplicationsMapper.toDTO(logsApplications.getStatusApplications()),
                logsApplications.getCreationDate());
    }

    public static LogsApplications toEntity(LogsApplicationsDTO logsApplicationsDTO) {
        LogsApplications logsApplications = new LogsApplications();
        logsApplications.setId(logsApplicationsDTO.getId());
        logsApplications.setUser(UserMapper.toEntity(logsApplicationsDTO.getUserDTO()));
        logsApplications.setApplications(ApplicationsMapper.toEntity(logsApplicationsDTO.getApplicationsDTO()));
        logsApplications.setStatusApplications(StatusApplicationsMapper.toEntity(logsApplicationsDTO.getStatusApplicationsDTO()));
        logsApplications.setCreationDate(logsApplicationsDTO.getCreationDate());
        return logsApplications;
    }
}
