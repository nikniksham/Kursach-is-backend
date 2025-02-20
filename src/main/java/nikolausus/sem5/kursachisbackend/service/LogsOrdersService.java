package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.LogsOrders;
import nikolausus.sem5.kursachisbackend.entity.Orders;
import nikolausus.sem5.kursachisbackend.entity.StatusOrders;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.CommentOnOrdersRepository;
import nikolausus.sem5.kursachisbackend.repository.LogsOrdersRepository;
import nikolausus.sem5.kursachisbackend.repository.OrdersRepository;
import org.aspectj.weaver.ast.Or;
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

    public boolean checkLastLogStatus(User user, Orders orders, StatusOrders statusOrder, int nomer) {
        List<LogsOrders> logsOrders = logsOrdersRepository.getLogsOrdersByOrders(orders);
        if (logsOrders.size() >= nomer) {
            LogsOrders log = logsOrders.get(logsOrders.size() - nomer);
//            System.out.println(orders.getId() + " " + user.getId() + " " + log.getUser().getId());
            return log.getUser().equals(user) && log.getStatusOrders().equals(statusOrder);
        }
        return false;
    }

}
