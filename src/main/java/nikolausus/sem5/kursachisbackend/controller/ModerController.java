package nikolausus.sem5.kursachisbackend.controller;

import nikolausus.sem5.kursachisbackend.DTO.CommentOnOrdersDTO;
import nikolausus.sem5.kursachisbackend.DTO.LogsOrdersDTO;
import nikolausus.sem5.kursachisbackend.DTO.OrdersDTO;
import nikolausus.sem5.kursachisbackend.DTO.UserDTO;
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
    public List<OrdersDTO> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @GetMapping("/orders/getById")
    public OrdersDTO getOrderById(@RequestParam Long order_id) {
        return ordersService.getOrderById(order_id);
    }

    @GetMapping("/orders/getAllLogs")
    public List<LogsOrdersDTO> getAllOrderLogs(@RequestParam Long order_id) {
        return logsOrdersService.getAllLogsByOrderId(order_id);
    }

    @GetMapping("/comment/getById")
    public CommentOnOrdersDTO getCommentByOrderId(@RequestParam Long order_id) {
        return commentOnOrdersService.getCommentByOrderId(ordersService.getOrderById(order_id));
    }

    @PostMapping("/orders/reject")
    public String rejectOrder(@RequestParam Long order_id, @RequestParam String why_not, @RequestHeader("Authorization") String jwt) {
        try {
            jwt = jwt.substring(7);
            UserDTO userDTO = userService.getUserByLogin(jwtUtil.extractUsername(jwt));
            Long user_id = userDTO.getId();
            OrdersDTO ordersDTO = ordersService.getOrderById(order_id);
            if (!ordersDTO.getStatusOrdersDTO().getId().equals(1L) && !ordersDTO.getStatusOrdersDTO().getId().equals(2L)) {
                throw new RuntimeException("Харам менять этот order");
            }
            ordersDTO.setStatusOrdersDTO(statusOrdersService.getStatusOrdersById(2L));
            ordersService.saveOrder(ordersDTO);
            logsOrdersService.createLog(user_id, order_id);

            try {
                CommentOnOrdersDTO commentOnOrdersDTO = commentOnOrdersService.getCommentByOrderId(ordersDTO);
                commentOnOrdersService.editCommentOnOrder(why_not, commentOnOrdersDTO.getId());
            } catch (Exception e) {
                commentOnOrdersService.createCommentOnOrder(order_id, userDTO, why_not);
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
            Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).getId();
            OrdersDTO ordersDTO = ordersService.getOrderById(order_id);
            if (!ordersDTO.getStatusOrdersDTO().getId().equals(1L) && !ordersDTO.getStatusOrdersDTO().getId().equals(2L)) {
                throw new RuntimeException("Харам менять этот order");
            }
            ordersDTO.setStatusOrdersDTO(statusOrdersService.getStatusOrdersById(3L));
            ordersService.saveOrder(ordersDTO);
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
            Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).getId();
            OrdersDTO ordersDTO = ordersService.getOrderById(order_id);
            if (!ordersDTO.getStatusOrdersDTO().getId().equals(5L)) {
                throw new RuntimeException("Этот заказ нельзя подтвердить");
            }
            ordersDTO.setStatusOrdersDTO(statusOrdersService.getStatusOrdersById(6L));
            ordersService.saveOrder(ordersDTO);
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
            Long user_id = userService.getUserByLogin(jwtUtil.extractUsername(jwt)).getId();
            OrdersDTO ordersDTO = ordersService.getOrderById(order_id);
            if (!ordersDTO.getStatusOrdersDTO().getId().equals(5L)) {
                throw new RuntimeException("Этот заказ нельзя НЕ подтвердить");
            }
            ordersDTO.setStatusOrdersDTO(statusOrdersService.getStatusOrdersById(3L));
            ordersService.saveOrder(ordersDTO);
            logsOrdersService.createLog(user_id, order_id);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Выполнение заказа отменено";
    }
}
