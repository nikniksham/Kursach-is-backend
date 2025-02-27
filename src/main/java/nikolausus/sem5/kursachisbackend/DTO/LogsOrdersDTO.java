package nikolausus.sem5.kursachisbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@AllArgsConstructor
@Getter
@Setter
public class LogsOrdersDTO {
    private Long id;
    private UserDTO userDTO;
    private OrdersDTO ordersDTO;
    private StatusOrdersDTO statusOrdersDTO;
    private Timestamp creationDate;
}
