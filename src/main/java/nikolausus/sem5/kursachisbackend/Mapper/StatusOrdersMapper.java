package nikolausus.sem5.kursachisbackend.Mapper;

import nikolausus.sem5.kursachisbackend.DTO.StatusOrdersDTO;
import nikolausus.sem5.kursachisbackend.entity.StatusOrders;

public class StatusOrdersMapper {
    public static StatusOrdersDTO toDTO(StatusOrders statusOrders) {
        return new StatusOrdersDTO(statusOrders.getId(), statusOrders.getStatus());
    }

    public static StatusOrders toEntity(StatusOrdersDTO statusOrdersDTO) {
        StatusOrders statusOrders = new StatusOrders();
        statusOrders.setStatus(statusOrdersDTO.getStatus());
        statusOrders.setId(statusOrdersDTO.getId());
        return statusOrders;
    }
}
