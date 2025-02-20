package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.entity.Orders;
import nikolausus.sem5.kursachisbackend.service.OrdersService;
import org.aspectj.weaver.ast.Or;
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
    public List<Orders> getAllOrders() {
        List<Orders> orders = new ArrayList<>();
        for (Orders ord : ordersService.getAllOrders()) {
            if (ord.getStatusOrders().getId() == 3 || ord.getStatusOrders().getId() == 4 || ord.getStatusOrders().getId() == 6) {
                orders.add(ord);
            }
        }
        return orders;
    }

    @GetMapping("/orders/getById")
    public Orders getOrderById(@RequestParam Long order_id) {
        Orders orders = ordersService.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Не найден заказ"));
        if (orders.getStatusOrders().getId() != 3 && orders.getStatusOrders().getId() != 4 && orders.getStatusOrders().getId() != 6) {
            throw new RuntimeException("С таким статусом нельзя");
        }
        return orders;
    }
}
