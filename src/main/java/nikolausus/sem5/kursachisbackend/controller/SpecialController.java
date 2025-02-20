package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.entity.Orders;
import nikolausus.sem5.kursachisbackend.entity.User;
import nikolausus.sem5.kursachisbackend.jwt.JwtUtil;
import nikolausus.sem5.kursachisbackend.service.LogsOrdersService;
import nikolausus.sem5.kursachisbackend.service.OrdersService;
import nikolausus.sem5.kursachisbackend.service.StatusOrdersService;
import nikolausus.sem5.kursachisbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/special")
public class SpecialController {
    private final OrdersService ordersService;
    private final StatusOrdersService statusOrdersService;
    private final LogsOrdersService logsOrdersService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public SpecialController(OrdersService ordersService, StatusOrdersService statusOrdersService, LogsOrdersService logsOrdersService, UserService userService, JwtUtil jwtUtil) {
        this.ordersService = ordersService;
        this.statusOrdersService = statusOrdersService;
        this.logsOrdersService = logsOrdersService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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
        return ordersService.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Не найден заказ"));
    }

    @GetMapping("/orders/finish")
    public String finishOrderById(@RequestParam Long order_id, @RequestHeader("Authorization") String jwt) {
        try {
            jwt = jwt.substring(7);
            User user = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow();
            Orders ord = ordersService.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Не найден заказ"));
            if (logsOrdersService.checkLastLogStatus(user, ord, statusOrdersService.getStatusOrdersById(4L).orElseThrow(), 1)) {
                ord.setStatusOrders(statusOrdersService.getStatusOrdersById(5L).orElseThrow());
                ordersService.saveOrder(ord);
                logsOrdersService.createLog(user.getId(), order_id);
                return "Выполнение ждёт подтверждения";
            }
            return "Чё то тут не чисто";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/orders/startWork")
    public String startWorkOrderById(@RequestParam Long order_id, @RequestHeader("Authorization") String jwt) {
        try {
            jwt = jwt.substring(7);
            Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();
            Orders orders = ordersService.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Не найден заказ"));
            if (orders.getStatusOrders().getId() != 3) {
                throw new RuntimeException("Заказ с таким статутсом нельзя взять в работу");
            }
            orders.setStatusOrders(statusOrdersService.getStatusOrdersById(4L).orElseThrow());
            ordersService.saveOrder(orders);
            logsOrdersService.createLog(user_id, order_id);
            return "Заказ выполнен успешно взят в работу";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/orders/what_i_do")
    public List<Orders> getWhatIDo(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.substring(7);
        User user = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow();
        List<Orders> orders = new ArrayList<>();
        for (Orders ord : ordersService.getAllOrders()) {
            if (ord.getStatusOrders().getId() == 4) {
                if (logsOrdersService.checkLastLogStatus(user, ord, statusOrdersService.getStatusOrdersById(4L).orElseThrow(), 1)) {
                    orders.add(ord);
                }
            }
        }
        return orders;
    }

    @GetMapping("/orders/portfolio")
    public List<Orders> portfolio(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.substring(7);
        User user = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow();
        List<Orders> orders = new ArrayList<>();
        for (Orders ord : ordersService.getAllOrders()) {
            if (ord.getStatusOrders().getId() == 6) {
                if (logsOrdersService.checkLastLogStatus(user, ord, statusOrdersService.getStatusOrdersById(5L).orElseThrow(), 2)) {
                    orders.add(ord);
                }
            }
        }
        return orders;
    }

    @GetMapping("/orders/reject")
    public String reject(@RequestHeader("Authorization") String jwt, @RequestParam Long order_id) {
        try {
            jwt = jwt.substring(7);
            User user = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow();
            Orders ord = ordersService.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Не найден заказ"));
            if (logsOrdersService.checkLastLogStatus(user, ord, statusOrdersService.getStatusOrdersById(4L).orElseThrow(), 1)) {
                ord.setStatusOrders(statusOrdersService.getStatusOrdersById(3L).orElseThrow());
                ordersService.saveOrder(ord);
                logsOrdersService.createLog(user.getId(), order_id);
                return "Вы отказались от заказа";
            }
            return "Вы не можете отказаться от этого заказа";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
