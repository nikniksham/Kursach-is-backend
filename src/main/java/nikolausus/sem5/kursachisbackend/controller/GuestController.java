package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.DTO.OrdersDTO;
import nikolausus.sem5.kursachisbackend.service.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/guest")
public class GuestController {
    private final OrdersService ordersService;

    public GuestController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/orders/all")
    public List<OrdersDTO> getAllOrders() {
        List<OrdersDTO> orders = new ArrayList<>();
        for (OrdersDTO ord : ordersService.getAllOrders()) {
            if (ord.getStatusOrdersDTO().getId() == 3 || ord.getStatusOrdersDTO().getId() == 4 ||
                    ord.getStatusOrdersDTO().getId() == 5 || ord.getStatusOrdersDTO().getId() == 6) {
                orders.add(ord);
            }
        }
        return orders;
    }

    @GetMapping("/orders/getById")
    public OrdersDTO getOrderById(@RequestParam Long order_id) {
        OrdersDTO ordersDTO = ordersService.getOrderById(order_id);
        if (ordersDTO.getStatusOrdersDTO().getId() != 3 && ordersDTO.getStatusOrdersDTO().getId() != 4 && ordersDTO.getStatusOrdersDTO().getId() != 5 && ordersDTO.getStatusOrdersDTO().getId() != 6) {
            throw new RuntimeException("С таким статусом нельзя");
        }
        return ordersDTO;
    }
}
