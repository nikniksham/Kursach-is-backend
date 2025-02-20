package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.entity.Orders;
import nikolausus.sem5.kursachisbackend.service.OrdersService;
import nikolausus.sem5.kursachisbackend.service.StatusOrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/special")
public class SpecialController {
    private final OrdersService ordersService;
    private final StatusOrdersService statusOrdersService;

    public SpecialController(OrdersService ordersService, StatusOrdersService statusOrdersService) {
        this.ordersService = ordersService;
        this.statusOrdersService = statusOrdersService;
    }

    @GetMapping("/orders/all")
    public List<Orders> getAllOrders() {
        List<Orders> orders = new ArrayList<>();
        for (Orders ord : ordersService.getAllOrders()) {
            if (ord.getStatusOrders().getId() == 3) {
                orders.add(ord);
            }
        }
        return orders;
    }

    @GetMapping("/orders/getById")
    public Orders getOrderById(@RequestParam Long order_id) {
        Orders orders = ordersService.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Не найден заказ"));
        if (orders.getStatusOrders().getId() != 3) {
            throw new RuntimeException("Заказ с таким статутсом нельзя выполнить");
        }
        return orders;
    }

    @GetMapping("/orders/finish")
    public String finishOrderById(@RequestParam Long order_id) {
        try {
            Orders orders = ordersService.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Не найден заказ"));
            if (orders.getStatusOrders().getId() != 3) {
                throw new RuntimeException("Заказ с таким статутсом нельзя выполнить");
            }
            orders.setStatusOrders(statusOrdersService.getStatusOrdersById(4L).orElseThrow());
            ordersService.saveOrder(orders);
            return "Заказ выполнен успешно";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
