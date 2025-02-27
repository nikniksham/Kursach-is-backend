package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.DTO.CommentOnOrdersDTO;
import nikolausus.sem5.kursachisbackend.DTO.OrdersDTO;
import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
import nikolausus.sem5.kursachisbackend.Mapper.CommentOnOrdersMapper;
import nikolausus.sem5.kursachisbackend.Mapper.OrdersMapper;
import nikolausus.sem5.kursachisbackend.Mapper.UserMapper;
import nikolausus.sem5.kursachisbackend.entity.CommentOnOrders;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.CommentOnOrdersRepository;
import nikolausus.sem5.kursachisbackend.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentOnOrdersService {
    private final CommentOnOrdersRepository commentOnOrdersRepository;
    private final OrdersRepository ordersRepository;

    public CommentOnOrdersService(CommentOnOrdersRepository commentOnOrdersRepository, OrdersRepository ordersRepository) {
        this.commentOnOrdersRepository = commentOnOrdersRepository;
        this.ordersRepository = ordersRepository;
    }

    public CommentOnOrdersDTO getCommentByOrderId(OrdersDTO ordersDTO) {
        return CommentOnOrdersMapper.toDTO(
                commentOnOrdersRepository.getCommentOnOrdersByOrders(OrdersMapper.toEntity(ordersDTO))
                        .orElseThrow(()->new RuntimeException("Комментарий на этот заказ не найден")));
    }

    public CommentOnOrders createCommentOnOrder(Long order_id, UserDTO userDTO, String text) {
        CommentOnOrders commentOnOrders = new CommentOnOrders();
        commentOnOrders.setOrders(ordersRepository.getOrdersById(order_id).orElseThrow());
        commentOnOrders.setUser(UserMapper.toEntity(userDTO));
        commentOnOrders.setText(text);
        return commentOnOrdersRepository.save(commentOnOrders);
    }

    public CommentOnOrders editCommentOnOrder(String text, Long comment_id) {
        CommentOnOrders commentOnOrders = commentOnOrdersRepository.getCommentOnOrdersById(comment_id).orElseThrow();
        commentOnOrders.setText(text);
        return commentOnOrdersRepository.save(commentOnOrders);
    }
}
