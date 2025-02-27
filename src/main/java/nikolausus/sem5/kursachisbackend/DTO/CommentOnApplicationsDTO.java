package nikolausus.sem5.kursachisbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentOnApplicationsDTO {
    private Long id;
    private String text;
    private ApplicationsDTO applicationsDTO;
    private UserDTO userDTO;
}
