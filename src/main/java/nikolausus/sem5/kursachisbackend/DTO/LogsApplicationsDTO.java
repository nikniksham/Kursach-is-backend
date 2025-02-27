package nikolausus.sem5.kursachisbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class LogsApplicationsDTO {
    private Long id;
    private UserDTO userDTO;
    private ApplicationsDTO applicationsDTO;
    private StatusApplicationsDTO statusApplicationsDTO;
    private Timestamp creationDate;
}
