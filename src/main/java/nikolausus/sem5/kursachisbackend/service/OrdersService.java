package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.entity.Orders;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.LogsOrdersRepository;
import nikolausus.sem5.kursachisbackend.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final StatusOrdersService statusOrdersService;
    private final LogsOrdersRepository logsOrdersRepository;

    public OrdersService(OrdersRepository ordersRepository, StatusOrdersService statusOrdersService, LogsOrdersRepository logsOrdersRepository) {
        this.ordersRepository = ordersRepository;
        this.statusOrdersService = statusOrdersService;
        this.logsOrdersRepository = logsOrdersRepository;
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public Optional<Orders> getOrderById(Long id) {
        return ordersRepository.findById(id);
    }

    public Orders createOrders(String target_name, Integer target_isu_num, String description, User user) {
        Orders new_orders = new Orders();
        new_orders.setTarget_name(target_name);
        new_orders.setTarget_isu_num(target_isu_num);
        new_orders.setDescription(description);
        new_orders.setUser(user);
        new_orders.setStatusOrders(statusOrdersService.getStatusOrdersById(1l).orElseThrow(() -> new RuntimeException("Статус не найден")));
        return ordersRepository.save(new_orders);
    }

    public boolean existOnTargetIsu(Integer target_isu) {
        boolean res = false;
        for (Orders orders : ordersRepository.findAll()) {
            if (orders.getTarget_isu_num().equals(target_isu)) {
                res = true;
                break;
            }
        }
        return res;
    }

    public List<Orders> getAllOrdersByUserId(User user) {
        return ordersRepository.getAllByUser(user);
    }

    public String updateOrder(Long order_id, User user, String pochemy, Integer targetIsuNum, String targetName) {
        Orders orders = this.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Заказ не найден"));
        if (!orders.getUser().equals(user)) {
            return "Пользователь не владелец заявки";
        }
        orders.setTarget_name(targetName);
        orders.setTarget_isu_num(targetIsuNum);
        orders.setDescription(pochemy);
        orders.setStatusOrders(statusOrdersService.getStatusOrdersById(1L).orElseThrow(() -> new RuntimeException("Статус не найден")));
        ordersRepository.save(orders);
        logsOrdersRepository.createLog(user.getId(), order_id);
        return "Всё прошло успешно";
    }

    public Orders saveOrder(Orders orders) {
        return ordersRepository.save(orders);
    }
}
