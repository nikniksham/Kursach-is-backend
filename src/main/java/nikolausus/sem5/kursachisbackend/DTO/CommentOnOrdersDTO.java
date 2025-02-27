package nikolausus.sem5.kursachisbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentOnOrdersDTO {
    private Long id;
    private String text;
    private OrdersDTO ordersDTO;
    private UserDTO userDTO;
}
