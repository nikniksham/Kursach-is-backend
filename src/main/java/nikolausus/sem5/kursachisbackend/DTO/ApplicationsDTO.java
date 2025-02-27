package nikolausus.sem5.kursachisbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationsDTO {
    private Long id;
    private UserDTO userDTO;
    private RoleDTO roleDTO;
    private StatusApplicationsDTO statusApplicationsDTO;
    private String text;
}
