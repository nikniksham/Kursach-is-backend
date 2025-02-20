package nikolausus.sem5.kursachisbackend.repository;

import nikolausus.sem5.kursachisbackend.entity.StatusOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusOrdersRepository extends JpaRepository<StatusOrders, Long> {
}
