package nikolausus.sem5.kursachisbackend.Mapper;

import nikolausus.sem5.kursachisbackend.DTO.CommentOnOrdersDTO;
import nikolausus.sem5.kursachisbackend.entity.CommentOnOrders;

public class CommentOnOrdersMapper {
    public static CommentOnOrdersDTO toDTO(CommentOnOrders commentOnOrders) {
        return new CommentOnOrdersDTO(
                commentOnOrders.getId(),
                commentOnOrders.getText(),
                OrdersMapper.toDTO(commentOnOrders.getOrders()),
                UserMapper.toDTO(commentOnOrders.getUser())
        );
    }

    public static CommentOnOrders toEntity(CommentOnOrdersDTO commentOnOrdersDTO) {
        CommentOnOrders commentOnOrders = new CommentOnOrders();
        commentOnOrders.setId(commentOnOrdersDTO.getId());
        commentOnOrders.setText(commentOnOrdersDTO.getText());
        commentOnOrders.setOrders(OrdersMapper.toEntity(commentOnOrdersDTO.getOrdersDTO()));
        commentOnOrders.setUser(UserMapper.toEntity(commentOnOrdersDTO.getUserDTO()));
        return commentOnOrders;
    }
}
