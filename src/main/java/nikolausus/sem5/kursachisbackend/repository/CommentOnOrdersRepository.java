package nikolausus.sem5.kursachisbackend.repository;

import nikolausus.sem5.kursachisbackend.entity.CommentOnApplications;
import nikolausus.sem5.kursachisbackend.entity.CommentOnOrders;
import nikolausus.sem5.kursachisbackend.entity.Orders;
import nikolausus.sem5.kursachisbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentOnOrdersRepository extends JpaRepository<CommentOnOrders, Long> {
    public Optional<CommentOnOrders> getCommentOnOrdersByOrders(Orders orders);
    public Optional<CommentOnOrders> getCommentOnOrdersById(Long id);
}
