package nikolausus.sem5.kursachisbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class OrdersDTO {
    private Long id;
    private String target_name;
    private Integer target_isu_num;
    private String description;
    private UserDTO userDTO;
    private Timestamp publication_date;
    private StatusOrdersDTO statusOrdersDTO;
}
