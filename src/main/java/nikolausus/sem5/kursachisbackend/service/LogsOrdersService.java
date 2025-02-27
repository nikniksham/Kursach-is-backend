package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.DTO.LogsOrdersDTO;
import nikolausus.sem5.kursachisbackend.DTO.OrdersDTO;
import nikolausus.sem5.kursachisbackend.DTO.StatusOrdersDTO;
import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
import nikolausus.sem5.kursachisbackend.Mapper.LogsOrdersMapper;
import nikolausus.sem5.kursachisbackend.Mapper.OrdersMapper;
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
import java.util.stream.Collectors;

@Service
public class LogsOrdersService {
    private final LogsOrdersRepository logsOrdersRepository;
    private final OrdersRepository ordersRepository;

    public LogsOrdersService(LogsOrdersRepository logsOrdersRepository, OrdersRepository ordersRepository1) {
        this.logsOrdersRepository = logsOrdersRepository;
        this.ordersRepository = ordersRepository1;
    }

    public List<LogsOrdersDTO> getAllLogsByOrderId(Long order_id) {
        Orders orders = ordersRepository.getOrdersById(order_id).orElseThrow();
        return this.logsOrdersRepository.getLogsOrdersByOrders(orders).stream().map(LogsOrdersMapper::toDTO).collect(Collectors.toList());
    }
    public void createLog(Long whoId, Long orderId) {
        logsOrdersRepository.createLog(whoId, orderId);
    }

    public boolean checkLastLogStatus(UserDTO userDTO, OrdersDTO ordersDTO, StatusOrdersDTO statusOrdersDTO, int nomer) {
        List<LogsOrders> logsOrders = logsOrdersRepository.getLogsOrdersByOrders(OrdersMapper.toEntity(ordersDTO));
        if (logsOrders.size() >= nomer) {
            LogsOrders log = logsOrders.get(logsOrders.size() - nomer);
//            System.out.println(orders.getId() + " " + user.getId() + " " + log.getUser().getId());
            return log.getUser().getId().equals(userDTO.getId()) && log.getStatusOrders().getStatus().equals(statusOrdersDTO.getStatus());
        }
        return false;
    }

}
