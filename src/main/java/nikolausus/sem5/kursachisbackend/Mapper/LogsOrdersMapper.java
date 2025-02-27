package nikolausus.sem5.kursachisbackend.Mapper;

import nikolausus.sem5.kursachisbackend.DTO.LogsOrdersDTO;
import nikolausus.sem5.kursachisbackend.entity.LogsOrders;

public class LogsOrdersMapper {
    public static LogsOrdersDTO toDTO(LogsOrders logsOrders) {
        return new LogsOrdersDTO(
                logsOrders.getId(),
                UserMapper.toDTO(logsOrders.getUser()),
                OrdersMapper.toDTO(logsOrders.getOrders()),
                StatusOrdersMapper.toDTO(logsOrders.getStatusOrders()),
                logsOrders.getCreationDate());
    }

    public static LogsOrders toEntity(LogsOrdersDTO logsOrdersDTO) {
        LogsOrders logsOrders = new LogsOrders();
        logsOrders.setId(logsOrdersDTO.getId());
        logsOrders.setUser(UserMapper.toEntity(logsOrdersDTO.getUserDTO()));
        logsOrders.setOrders(OrdersMapper.toEntity(logsOrdersDTO.getOrdersDTO()));
        logsOrders.setStatusOrders(StatusOrdersMapper.toEntity(logsOrdersDTO.getStatusOrdersDTO()));
        logsOrders.setCreationDate(logsOrdersDTO.getCreationDate());
        return logsOrders;
    }
}
