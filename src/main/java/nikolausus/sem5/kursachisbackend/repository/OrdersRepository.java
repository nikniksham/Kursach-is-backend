package nikolausus.sem5.kursachisbackend.repository;

import nikolausus.sem5.kursachisbackend.entity.Orders;
import nikolausus.sem5.kursachisbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    public Optional<Orders> getOrdersById(Long id);
    List<Orders> getAllByUser(User user);
}
