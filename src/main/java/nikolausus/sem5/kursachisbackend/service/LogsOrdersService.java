package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.LogsOrders;
import nikolausus.sem5.kursachisbackend.entity.Orders;
import nikolausus.sem5.kursachisbackend.repository.CommentOnOrdersRepository;
import nikolausus.sem5.kursachisbackend.repository.LogsOrdersRepository;
import nikolausus.sem5.kursachisbackend.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogsOrdersService {
    private final LogsOrdersRepository logsOrdersRepository;
    private final OrdersRepository ordersRepository;

    public LogsOrdersService(LogsOrdersRepository logsOrdersRepository, OrdersRepository ordersRepository1) {
        this.logsOrdersRepository = logsOrdersRepository;
        this.ordersRepository = ordersRepository1;
    }

    public List<LogsOrders> getAllLogsByOrderId(Long order_id) {
        Orders orders = ordersRepository.getOrdersById(order_id).orElseThrow();
        return this.logsOrdersRepository.getLogsOrdersByOrders(orders);
    }
    public void createLog(Long whoId, Long orderId) {
        logsOrdersRepository.createLog(whoId, orderId);
    }

}
