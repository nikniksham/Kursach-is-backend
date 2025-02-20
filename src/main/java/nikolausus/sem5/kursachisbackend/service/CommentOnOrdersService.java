package nikolausus.sem5.kursachisbackend.service;

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

    public Optional<CommentOnOrders> getCommentByOrderId(Long order_id) {
        return commentOnOrdersRepository.getCommentOnOrdersByOrders(ordersRepository.getOrdersById(order_id).orElseThrow());
    }

    public CommentOnOrders createCommentOnOrder(Long order_id, User user, String text) {
        CommentOnOrders commentOnOrders = new CommentOnOrders();
        commentOnOrders.setOrders(ordersRepository.getOrdersById(order_id).orElseThrow());
        commentOnOrders.setUser(user);
        commentOnOrders.setText(text);
        return commentOnOrdersRepository.save(commentOnOrders);
    }

    public CommentOnOrders editCommentOnOrder(String text, Long comment_id) {
        CommentOnOrders commentOnOrders = commentOnOrdersRepository.getCommentOnOrdersById(comment_id).orElseThrow();
        commentOnOrders.setText(text);
        return commentOnOrdersRepository.save(commentOnOrders);
    }
}
