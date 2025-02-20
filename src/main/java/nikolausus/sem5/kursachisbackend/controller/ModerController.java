package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.entity.*;
import nikolausus.sem5.kursachisbackend.jwt.JwtUtil;
import nikolausus.sem5.kursachisbackend.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/moder")
public class ModerController {
    private final OrdersService ordersService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final LogsOrdersService logsOrdersService;
    private final StatusOrdersService statusOrdersService;
    private final CommentOnOrdersService commentOnOrdersService;

    public ModerController(OrdersService ordersService, JwtUtil jwtUtil, UserService userService, LogsOrdersService logsOrdersService, StatusOrdersService statusOrdersService, CommentOnOrdersService commentOnOrdersService) {
        this.ordersService = ordersService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.logsOrdersService = logsOrdersService;
        this.statusOrdersService = statusOrdersService;
        this.commentOnOrdersService = commentOnOrdersService;
    }

    @GetMapping("/orders/all")
    public List<Orders> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @GetMapping("/orders/getById")
    public Orders getOrderById(@RequestParam Long order_id) {
        return ordersService.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Не найден заказ"));
    }

    @GetMapping("/orders/getAllLogs")
    public List<LogsOrders> getAllOrderLogs(@RequestParam Long order_id) {
        return logsOrdersService.getAllLogsByOrderId(order_id);
    }

    @GetMapping("/comment/getById")
    public CommentOnOrders getCommentByApplicationId(@RequestParam Long order_id) {
        return commentOnOrdersService.getCommentByOrderId(order_id).orElseThrow();
    }

    @PostMapping("/orders/reject")
    public String rejectOrder(@RequestParam Long order_id, @RequestParam String why_not, @RequestHeader("Authorization") String jwt) {
        try {
            jwt = jwt.substring(7);
            User user = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            Long user_id = user.getId();
            Orders orders = ordersService.getOrderById(order_id).orElseThrow(() -> new RuntimeException("Нету"));
            if (!orders.getStatusOrders().getId().equals(1L) && !orders.getStatusOrders().getId().equals(2L)) {
                throw new RuntimeException("Харам менять этот order");
            }
            orders.setStatusOrders(statusOrdersService.getStatusOrdersById(2L).orElseThrow(() -> new RuntimeException("Нет такого статуса")));
            ordersService.saveOrder(orders);
            logsOrdersService.createLog(user_id, order_id);

            Optional<CommentOnOrders> comment = commentOnOrdersService.getCommentByOrderId(order_id);
            if (comment.isPresent()) {
                commentOnOrdersService.editCommentOnOrder(why_not, comment.get().getId());
            } else {
                commentOnOrdersService.createCommentOnOrder(order_id, user, why_not);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Заказ отклонён";
    }

    @PostMapping("/orders/approve")
    public String approveOrder(@RequestParam Long order_id, @RequestHeader("Authorization") String jwt) {
        try {
            jwt = jwt.substring(7);
            Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();
            Orders orders = ordersService.getOrderById(order_id).orElseThrow();
            if (!orders.getStatusOrders().getId().equals(1L) && !orders.getStatusOrders().getId().equals(2L)) {
                throw new RuntimeException("Харам менять этот order");
            }
            orders.setStatusOrders(statusOrdersService.getStatusOrdersById(3L).orElseThrow());
            ordersService.saveOrder(orders);
            logsOrdersService.createLog(user_id, order_id);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Заказ одобрен";
    }

    @PostMapping("/orders/makeOk")
    public String makeOk(@RequestParam Long order_id, @RequestHeader("Authorization") String jwt) {
        try {
            jwt = jwt.substring(7);
            Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();
            Orders orders = ordersService.getOrderById(order_id).orElseThrow();
            if (!orders.getStatusOrders().getId().equals(5L)) {
                throw new RuntimeException("Этот заказ нельзя подтвердить");
            }
            orders.setStatusOrders(statusOrdersService.getStatusOrdersById(6L).orElseThrow());
            ordersService.saveOrder(orders);
            logsOrdersService.createLog(user_id, order_id);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Выполнение заказа подтверждено";
    }

    @PostMapping("/orders/makeBad")
    public String makeBad(@RequestParam Long order_id, @RequestHeader("Authorization") String jwt) {
        try {
            jwt = jwt.substring(7);
            Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).orElseThrow(() -> new RuntimeException("Пользователь не найден")).getId();
            Orders orders = ordersService.getOrderById(order_id).orElseThrow();
            if (!orders.getStatusOrders().getId().equals(5L)) {
                throw new RuntimeException("Этот заказ нельзя НЕ подтвердить");
            }
            orders.setStatusOrders(statusOrdersService.getStatusOrdersById(3L).orElseThrow());
            ordersService.saveOrder(orders);
            logsOrdersService.createLog(user_id, order_id);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Выполнение заказа отменено";
    }
}
