package nikolausus.sem5.kursachisbackend.Mapper;

import nikolausus.sem5.kursachisbackend.DTO.OrdersDTO;
import nikolausus.sem5.kursachisbackend.entity.Orders;

public class OrdersMapper {
    public static OrdersDTO toDTO(Orders orders) {
        return new OrdersDTO(
                orders.getId(),
                orders.getTarget_name(),
                orders.getTarget_isu_num(),
                orders.getDescription(),
                UserMapper.toDTO(orders.getUser()),
                orders.getPublication_date(),
                StatusOrdersMapper.toDTO(orders.getStatusOrders()));
    }

    public static Orders toEntity(OrdersDTO ordersDTO) {
        Orders orders = new Orders();
        orders.setId(ordersDTO.getId());
        orders.setTarget_name(ordersDTO.getTarget_name());
        orders.setTarget_isu_num(ordersDTO.getTarget_isu_num());
        orders.setDescription(ordersDTO.getDescription());
        orders.setUser(UserMapper.toEntity(ordersDTO.getUserDTO()));
        orders.setPublication_date(ordersDTO.getPublication_date());
        orders.setStatusOrders(StatusOrdersMapper.toEntity(ordersDTO.getStatusOrdersDTO()));
        return orders;
    }
}
