package nikolausus.sem5.kursachisbackend.repository;

import jakarta.transaction.Transactional;
import nikolausus.sem5.kursachisbackend.entity.LogsOrders;
import nikolausus.sem5.kursachisbackend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsOrdersRepository extends JpaRepository<LogsOrders, Long> {
    @Transactional
    @Query(value = "SELECT log_orders(:whoId, :ordId)", nativeQuery = true)
    void createLog(@Param("whoId") long whoId, @Param("ordId") long appId);

    List<LogsOrders> getLogsOrdersByOrders(Orders orders);
}
