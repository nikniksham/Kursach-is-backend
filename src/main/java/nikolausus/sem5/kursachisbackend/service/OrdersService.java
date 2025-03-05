package nikolausus.sem5.kursachisbackend.service;

import nikolausus.sem5.kursachisbackend.DTO.OrdersDTO;
import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
import nikolausus.sem5.kursachisbackend.Mapper.OrdersMapper;
import nikolausus.sem5.kursachisbackend.Mapper.StatusOrdersMapper;
import nikolausus.sem5.kursachisbackend.Mapper.UserMapper;
import nikolausus.sem5.kursachisbackend.entity.Applications;
import nikolausus.sem5.kursachisbackend.entity.Orders;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.repository.LogsOrdersRepository;
import nikolausus.sem5.kursachisbackend.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<OrdersDTO> getAllOrders() {
        return ordersRepository.findAll().stream().map(OrdersMapper::toDTO).collect(Collectors.toList());
    }

    public OrdersDTO getOrderById(Long id) {
        return OrdersMapper.toDTO(ordersRepository.findById(id).orElseThrow(()->new RuntimeException("Заказ по заданному id не найден")));
    }

    public OrdersDTO createOrders(String target_name, Integer target_isu_num, String description, UserDTO userDTO) {
        Orders new_orders = new Orders();
        new_orders.setTarget_name(target_name);
        new_orders.setTarget_isu_num(target_isu_num);
        new_orders.setDescription(description);
        new_orders.setUser(UserMapper.toEntity(userDTO));
        new_orders.setStatusOrders(StatusOrdersMapper.toEntity(statusOrdersService.getStatusOrdersById(1L)));
        return OrdersMapper.toDTO(ordersRepository.save(new_orders));
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

    public OrdersDTO getByTargetIsu(Integer target_isu) {
        OrdersDTO ordersDTO = null;
        for (Orders orders : ordersRepository.findAll()) {
            if (orders.getTarget_isu_num().equals(target_isu)) {
                ordersDTO = OrdersMapper.toDTO(orders);
                break;
            }
        }
        return ordersDTO;
    }

    public List<OrdersDTO> getAllOrdersByUserId(UserDTO userDTO) {
        return ordersRepository.getAllByUser(UserMapper.toEntity(userDTO)).stream().map(OrdersMapper::toDTO).collect(Collectors.toList());
    }

    public String updateOrder(Long order_id, UserDTO userDTO, String pochemy, Integer targetIsuNum, String targetName) {
        Orders orders = ordersRepository.findById(order_id).orElseThrow(() -> new RuntimeException("Заказ не найден"));
        if (!orders.getUser().getId().equals(userDTO.getId())) {
            return "Пользователь не владелец заявки";
        }
        orders.setTarget_name(targetName);
        orders.setTarget_isu_num(targetIsuNum);
        orders.setDescription(pochemy);
        orders.setStatusOrders(StatusOrdersMapper.toEntity(statusOrdersService.getStatusOrdersById(1L)));
        ordersRepository.save(orders);
        logsOrdersRepository.createLog(userDTO.getId(), order_id);
        return "Всё прошло успешно";
    }

    public Orders saveOrder(OrdersDTO ordersDTO) {
        return ordersRepository.save(OrdersMapper.toEntity(ordersDTO));
    }
}
