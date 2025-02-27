package nikolausus.sem5.kursachisbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String login;
    private String password;
    private Set<RoleDTO> rolesDTO;
}
